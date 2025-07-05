package com.zhsnddn.index12306.userservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.zhsnddn.index12306.framework.starter.common.toolkit.BeanUtil;
import com.zhsnddn.index12306.framework.starter.convention.exception.ServiceException;
import com.zhsnddn.index12306.userservice.dao.entity.UserDO;
import com.zhsnddn.index12306.userservice.dao.mapper.UserMapper;
import com.zhsnddn.index12306.userservice.dto.req.UserRegisterReqDTO;
import com.zhsnddn.index12306.userservice.dto.req.UserUpdateReqDTO;
import com.zhsnddn.index12306.userservice.dto.resp.UserRegisterRespDTO;
import com.zhsnddn.index12306.userservice.dto.resp.UserUpdateRespDTO;
import com.zhsnddn.index12306.userservice.service.UserLoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserLoginServiceImpl implements UserLoginService {


    private final UserMapper userMapper;

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
}
