package com.zhsnddn.index12306.userservice.service;

import com.zhsnddn.index12306.userservice.dto.req.UserRegisterReqDTO;
import com.zhsnddn.index12306.userservice.dto.resp.UserRegisterRespDTO;


public interface UserLoginService {

    /**
     * 注册用户
     */
    UserRegisterRespDTO register(UserRegisterReqDTO requestParam);
}
