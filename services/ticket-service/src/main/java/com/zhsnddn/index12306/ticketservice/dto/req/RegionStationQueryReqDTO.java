package com.zhsnddn.index12306.ticketservice.dto.req;

import lombok.Data;

/**
 * 地区以及车站查询请求参数
 */

@Data
public class RegionStationQueryReqDTO {

    /**
     * 查询方式
     */
    private Integer queryType;

    /**
     * 名称
     */
    private String name;
}
