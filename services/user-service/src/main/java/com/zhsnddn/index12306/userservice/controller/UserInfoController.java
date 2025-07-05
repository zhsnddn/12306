package com.zhsnddn.index12306.userservice.controller;

import com.zhsnddn.index12306.framework.starter.convention.result.Result;
import com.zhsnddn.index12306.framework.starter.web.Results;
import com.zhsnddn.index12306.userservice.dto.req.UserLoginReqDTO;
import com.zhsnddn.index12306.userservice.dto.req.UserRegisterReqDTO;
import com.zhsnddn.index12306.userservice.dto.req.UserUpdateReqDTO;
import com.zhsnddn.index12306.userservice.dto.resp.UserLoginRespDTO;
import com.zhsnddn.index12306.userservice.dto.resp.UserQueryRespDTO;
import com.zhsnddn.index12306.userservice.dto.resp.UserRegisterRespDTO;
import com.zhsnddn.index12306.userservice.dto.resp.UserUpdateRespDTO;
import com.zhsnddn.index12306.userservice.service.UserLoginService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class UserInfoController {

    private final UserLoginService userLoginService;

    /**
     * 注册用户
     */
    @PostMapping("/api/user-service/register")
    public Result<UserRegisterRespDTO> register(@RequestBody UserRegisterReqDTO requestParam) {
        return Results.success(userLoginService.register(requestParam));
    }

    /**
     * 检查用户名是否存在
     */
    @GetMapping("/api/user-service/has-username")
    public Result<Boolean> hasUsername(@RequestParam("username") @NotNull String username) {
        return Results.success(userLoginService.hasUsername(username));
    }

    /**
     * 更改用户信息
     */
    @PostMapping("/api/user-service/update")
    public Result<UserUpdateRespDTO> update(@RequestBody UserUpdateReqDTO requestParam) {
        return Results.success(userLoginService.update(requestParam));
    }

    /**
     * 查询用户信息
     */
    @GetMapping("/api/user-service/query")
    public Result<UserQueryRespDTO> query(@RequestParam("username") @NotNull String username) {
        return Results.success(userLoginService.query(username));
    }

    /**
     * 用户登录
     */
    @PostMapping("/api/user-service/login")
    public Result<UserLoginRespDTO> login(@RequestBody UserLoginReqDTO requestParam) {
        return Results.success(userLoginService.login(requestParam));
    }
}
