package com.zhsnddn.index12306.userservice.service;

import com.zhsnddn.index12306.userservice.dto.req.UserLoginReqDTO;
import com.zhsnddn.index12306.userservice.dto.req.UserRegisterReqDTO;
import com.zhsnddn.index12306.userservice.dto.req.UserUpdateReqDTO;
import com.zhsnddn.index12306.userservice.dto.resp.UserLoginRespDTO;
import com.zhsnddn.index12306.userservice.dto.resp.UserQueryRespDTO;
import com.zhsnddn.index12306.userservice.dto.resp.UserRegisterRespDTO;
import com.zhsnddn.index12306.userservice.dto.resp.UserUpdateRespDTO;
import jakarta.validation.constraints.NotNull;

/**
 * 用户登录接口层
 */

public interface UserLoginService {

    /**
     * 校验用户名是否存在
     */
    Boolean hasUsername(@NotNull String username);

    /**
     * 注册用户
     */
    UserRegisterRespDTO register(UserRegisterReqDTO requestParam);


    /**
     * 用户登录
     */
    UserLoginRespDTO login(UserLoginReqDTO requestParam);

    /**
     * 通过Token检查用户是否登录
     */
    UserLoginRespDTO checkLogin(String accessToken);
}
