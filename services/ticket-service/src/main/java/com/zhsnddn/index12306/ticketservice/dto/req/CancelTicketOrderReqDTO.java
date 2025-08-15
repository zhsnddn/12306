package com.zhsnddn.index12306.ticketservice.dto.req;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 取消车票订单请求入参
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CancelTicketOrderReqDTO {

    /**
     * 订单号
     */
    private String orderSn;
}
