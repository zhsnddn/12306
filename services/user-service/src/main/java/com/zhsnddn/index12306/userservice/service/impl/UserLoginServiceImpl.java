package com.zhsnddn.index12306.userservice.service.impl;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
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
import com.zhsnddn.index12306.userservice.dto.resp.UserLoginRespDTO;
import com.zhsnddn.index12306.userservice.dto.resp.UserRegisterRespDTO;
import com.zhsnddn.index12306.userservice.service.UserLoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static com.zhsnddn.index12306.userservice.common.enums.UserRegisterErrorCodeEnum.*;

/**
 * 用户登录接口实现
 */

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
//        if (hasUsername(userDO.getUsername())) {
//            throw new ServiceException(HAS_USERNAME_NOTNULL);
//        }
        int insert = userMapper.insert(userDO);
        if (insert < 1) {
            throw new ServiceException(USER_REGISTER_FAIL);
        }
        return BeanUtil.convert(requestParam, UserRegisterRespDTO.class);
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
                    .orElseThrow(() -> new ClientException(HAVE_NOT_USERNAME_OR_PHONE_OR_MAIL));
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
            throw new ClientException(USERNAME_OR_PASSWORD_ERROR);
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
