package com.zhsnddn.index12306.userservice.service;

import com.zhsnddn.index12306.userservice.dto.req.UserUpdateReqDTO;
import com.zhsnddn.index12306.userservice.dto.resp.UserQueryActualRespDTO;
import com.zhsnddn.index12306.userservice.dto.resp.UserQueryRespDTO;
import com.zhsnddn.index12306.userservice.dto.resp.UserUpdateRespDTO;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

/**
 * 用户服务接口层
 */
public interface  UserService {


    /**
     * 更改用户信息
     */
    UserUpdateRespDTO update(UserUpdateReqDTO requestParam);

    /**
     * 查询用户信息
     */
    UserQueryRespDTO query(@NotNull String username);

    /**
     * 根据用户名查询用户无脱敏信息
     */
    UserQueryActualRespDTO queryActualUserByUsername(@NotEmpty String username);
}
