package com.zhsnddn.index12306.userservice.dto.resp;

import lombok.Builder;
import lombok.Data;

/**
 * 用户更改信息响应参数
 */
@Data
@Builder
public class UserUpdateRespDTO {

    /**
     * 用户名
     */
    private String username;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 手机号
     */
    private String phone;
}
