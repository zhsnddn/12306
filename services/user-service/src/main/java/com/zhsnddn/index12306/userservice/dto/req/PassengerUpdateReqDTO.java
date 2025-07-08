package com.zhsnddn.index12306.userservice.dto.req;

import lombok.Data;

@Data
public class PassengerUpdateReqDTO {
    /**
     * 乘车人用户名
     */
    public String username;
    /**
     * 乘车人id
     */
    public String id;
    /**
     * 乘车人手机号
     */
    public String phone;
}
