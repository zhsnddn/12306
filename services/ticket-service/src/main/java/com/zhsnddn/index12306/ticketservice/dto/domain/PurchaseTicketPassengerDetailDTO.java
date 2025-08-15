package com.zhsnddn.index12306.ticketservice.dto.domain;

import lombok.Data;

/**
 * 购票乘车人实体
 */
@Data
public class PurchaseTicketPassengerDetailDTO {

    /**
     * 乘车人 ID
     */
    private String passengerId;

    /**
     * 座位类型
     */
    private Integer seatType;
}