package com.zhsnddn.index12306.userservice.controller;

import com.zhsnddn.index12306.framework.starter.convention.result.Result;
import com.zhsnddn.index12306.framework.starter.web.Results;
import com.zhsnddn.index12306.userservice.dto.req.UserUpdateReqDTO;
import com.zhsnddn.index12306.userservice.dto.resp.UserQueryActualRespDTO;
import com.zhsnddn.index12306.userservice.dto.resp.UserQueryRespDTO;
import com.zhsnddn.index12306.userservice.dto.resp.UserUpdateRespDTO;
import com.zhsnddn.index12306.userservice.service.UserService;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class UserInfoController {

    private final UserService userService;



    /**
     * 更改用户信息
     */
    @PostMapping("/api/user-service/update")
    public Result<UserUpdateRespDTO> update(@RequestBody UserUpdateReqDTO requestParam) {
        return Results.success(userService.update(requestParam));
    }

    /**
     * 查询用户信息
     */
    @GetMapping("/api/user-service/query")
    public Result<UserQueryRespDTO> query(@RequestParam("username") @NotNull String username) {
        return Results.success(userService.queryByUsername(username));
    }

    /**
     * 根据用户名查询用户无脱敏信息
     */
    @GetMapping("/api/user-service/actual/query")
    public Result<UserQueryActualRespDTO> queryActualUserByUsername(@RequestParam("username") @NotEmpty String username) {
        return Results.success(userService.queryActualUserByUsername(username));
    }


}
