package com.zhsnddn.index12306.ticketservice.dto.req;

import lombok.Data;

import java.util.List;

/**
 * 车票子订单查询
 * 公众号：马丁玩编程，回复：加群，添加马哥微信（备注：12306）获取项目资料
 */
@Data
public class TicketOrderItemQueryReqDTO {

    /**
     * 订单号
     */
    private String orderSn;

    /**
     * 子订单记录id
     */
    private List<String> orderItemRecordIds;
}
