package com.zhsnddn.index12306.userservice.service;

import com.zhsnddn.index12306.userservice.dto.req.UserLoginReqDTO;
import com.zhsnddn.index12306.userservice.dto.req.UserRegisterReqDTO;
import com.zhsnddn.index12306.userservice.dto.req.UserUpdateReqDTO;
import com.zhsnddn.index12306.userservice.dto.resp.UserLoginRespDTO;
import com.zhsnddn.index12306.userservice.dto.resp.UserQueryRespDTO;
import com.zhsnddn.index12306.userservice.dto.resp.UserRegisterRespDTO;
import com.zhsnddn.index12306.userservice.dto.resp.UserUpdateRespDTO;
import jakarta.validation.constraints.NotNull;


public interface UserLoginService {

    /**
     * 注册用户
     */
    UserRegisterRespDTO register(UserRegisterReqDTO requestParam);

    /**
     * 校验用户名是否存在
     */
    Boolean hasUsername(@NotNull String username);

    /**
     * 更改用户信息
     */
    UserUpdateRespDTO update(UserUpdateReqDTO requestParam);

    /**
     * 查询用户信息
     */
    UserQueryRespDTO query(@NotNull String username);

    /**
     * 用户登录
     */
    UserLoginRespDTO login(UserLoginReqDTO requestParam);
}
