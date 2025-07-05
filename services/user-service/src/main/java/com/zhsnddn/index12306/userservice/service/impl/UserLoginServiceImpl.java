package com.zhsnddn.index12306.userservice.service.impl;

import com.zhsnddn.index12306.framework.starter.common.toolkit.BeanUtil;
import com.zhsnddn.index12306.framework.starter.convention.exception.ServiceException;
import com.zhsnddn.index12306.userservice.dao.entity.UserDO;
import com.zhsnddn.index12306.userservice.dao.mapper.UserMapper;
import com.zhsnddn.index12306.userservice.dto.req.UserRegisterReqDTO;
import com.zhsnddn.index12306.userservice.dto.resp.UserRegisterRespDTO;
import com.zhsnddn.index12306.userservice.service.UserLoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserLoginServiceImpl implements UserLoginService {


    private final UserMapper userMapper;

    @Override
    public UserRegisterRespDTO register(UserRegisterReqDTO requestParam) {
        //TODO:判断用户是否存在
        UserDO userDO = BeanUtil.convert(requestParam, UserDO.class);
        int insert = userMapper.insert(userDO);
        if(insert < 1) {
            throw new ServiceException("注册失败");
        }
        return BeanUtil.convert(requestParam, UserRegisterRespDTO.class);
    }
}
