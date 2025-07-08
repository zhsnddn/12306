package com.zhsnddn.index12306.userservice.dto.req;

import lombok.Data;

/**
 * 乘车人删除请求参数
 */

@Data
public class PassengerRemoveReqDTO {

    /**
     * 乘车人用户名
     **/
    public String username;

    /**
     * 乘车人id
     */
    public String id;
}
