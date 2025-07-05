package com.zhsnddn.index12306.userservice.dto.resp;

import lombok.Builder;
import lombok.Data;
/**
 * 用户登录响应参数
 */
@Data
@Builder
public class UserLoginRespDTO {

    /**
     * 用户 ID
     */
    private String userId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * Token
     */
    private String accessToken;
}