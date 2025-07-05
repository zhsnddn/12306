package com.zhsnddn.index12306.userservice.service.impl;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.zhsnddn.index12306.framework.starter.cache.DistributedCache;
import com.zhsnddn.index12306.framework.starter.common.toolkit.BeanUtil;
import com.zhsnddn.index12306.framework.starter.convention.exception.ClientException;
import com.zhsnddn.index12306.framework.starter.convention.exception.ServiceException;
import com.zhsnddn.index12306.framework.starter.user.core.UserInfoDTO;
import com.zhsnddn.index12306.framework.starter.user.toolkit.JWTUtil;
import com.zhsnddn.index12306.userservice.dao.entity.UserDO;
import com.zhsnddn.index12306.userservice.dao.entity.UserMailDO;
import com.zhsnddn.index12306.userservice.dao.entity.UserPhoneDO;
import com.zhsnddn.index12306.userservice.dao.mapper.UserMailMapper;
import com.zhsnddn.index12306.userservice.dao.mapper.UserMapper;
import com.zhsnddn.index12306.userservice.dao.mapper.UserPhoneMapper;
import com.zhsnddn.index12306.userservice.dto.req.UserLoginReqDTO;
import com.zhsnddn.index12306.userservice.dto.req.UserRegisterReqDTO;
import com.zhsnddn.index12306.userservice.dto.req.UserUpdateReqDTO;
import com.zhsnddn.index12306.userservice.dto.resp.UserLoginRespDTO;
import com.zhsnddn.index12306.userservice.dto.resp.UserQueryRespDTO;
import com.zhsnddn.index12306.userservice.dto.resp.UserRegisterRespDTO;
import com.zhsnddn.index12306.userservice.dto.resp.UserUpdateRespDTO;
import com.zhsnddn.index12306.userservice.service.UserLoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class UserLoginServiceImpl implements UserLoginService {


    private final UserMapper userMapper;
    private final UserMailMapper userMailMapper;
    private final UserPhoneMapper userPhoneMapper;
    private final DistributedCache distributedCache;

    @Override
    public UserRegisterRespDTO register(UserRegisterReqDTO requestParam) {
        UserDO userDO = BeanUtil.convert(requestParam, UserDO.class);
        if (hasUsername(userDO.getUsername())) {
            throw new ServiceException("用户名已存在");
        }
        int insert = userMapper.insert(userDO);
        if (insert < 1) {
            throw new ServiceException("注册失败");
        }
        return BeanUtil.convert(requestParam, UserRegisterRespDTO.class);
    }

    @Override
    public Boolean hasUsername(String username) {
        LambdaQueryWrapper<UserDO> queryWrapper = Wrappers.lambdaQuery(UserDO.class)
                .eq(UserDO::getUsername, username);
        return userMapper.selectOne(queryWrapper) == null ? Boolean.FALSE : Boolean.TRUE;
    }

    @Override
    public UserUpdateRespDTO update(UserUpdateReqDTO requestParam) {
        UserDO userDO = BeanUtil.convert(requestParam, UserDO.class);
        LambdaUpdateWrapper<UserDO> updateWrapper = Wrappers.lambdaUpdate(UserDO.class)
                .eq(UserDO::getUsername, userDO.getUsername());
        userMapper.update(userDO, updateWrapper);
        UserUpdateRespDTO response = UserUpdateRespDTO.builder()
                .username(userDO.getUsername())
                .realName(userDO.getRealName())
                .phone(userDO.getPhone())
                .build();
        return response;
    }

    @Override
    public UserQueryRespDTO query(String username) {
        LambdaQueryWrapper<UserDO> queryWrapper = Wrappers.lambdaQuery(UserDO.class)
                .eq(UserDO::getUsername, username);
        UserDO userDO = userMapper.selectOne(queryWrapper);
        if (userDO == null) {
            throw new ServiceException("用户不存在");
        }
        return BeanUtil.convert(userDO, UserQueryRespDTO.class);
    }

    @Override
    public UserLoginRespDTO login(UserLoginReqDTO requestParam) {
        String usernameOrMailOrPhone = requestParam.getUsernameOrMailOrPhone();
        boolean mailFlag = false;
        // 检查邮箱格式
        for (char c : usernameOrMailOrPhone.toCharArray()) {
            if (c == '@') {
                mailFlag = true;
                break;
            }
        }
        String username;
        if (mailFlag) {
            LambdaQueryWrapper<UserMailDO> queryWrapper = Wrappers.lambdaQuery(UserMailDO.class)
                    .eq(UserMailDO::getMail, usernameOrMailOrPhone);
            username = Optional.ofNullable(userMailMapper.selectOne(queryWrapper))
                    .map(UserMailDO::getMail)
                    .orElseThrow(() -> new ClientException("用户名/手机号/邮箱不存在"));
        } else {
            LambdaQueryWrapper<UserPhoneDO> queryWrapper = Wrappers.lambdaQuery(UserPhoneDO.class)
                    .eq(UserPhoneDO::getPhone, usernameOrMailOrPhone);
            username = Optional.ofNullable(userPhoneMapper.selectOne(queryWrapper))
                    .map(UserPhoneDO::getPhone)
                    .orElse(null);
        }
        username = Optional.ofNullable(username).orElse(requestParam.getUsernameOrMailOrPhone());
        LambdaQueryWrapper<UserDO> queryWrapper = Wrappers.lambdaQuery(UserDO.class)
                .eq(UserDO::getUsername, username)
                .eq(UserDO::getPassword, requestParam.getPassword())
                .select(UserDO::getId, UserDO::getUsername, UserDO::getRealName);
        UserDO userDO = userMapper.selectOne(queryWrapper);
        if (userDO == null) {
            throw new ClientException("账号不存在或密码错误");
        }
        UserInfoDTO userInfo = UserInfoDTO.builder()
                .userId(String.valueOf(userDO.getId()))
                .username(userDO.getUsername())
                .realName(userDO.getRealName())
                .build();
        String accessToken = JWTUtil.generateAccessToken(userInfo);
        UserLoginRespDTO actual = UserLoginRespDTO.builder()
                .userId(userInfo.getUserId())
                .username(requestParam.getUsernameOrMailOrPhone())
                .realName(userInfo.getRealName())
                .accessToken(accessToken)
                .build();
        distributedCache.put(accessToken, JSON.toJSONString(actual), 30, TimeUnit.MINUTES);
        return actual;
    }
}
