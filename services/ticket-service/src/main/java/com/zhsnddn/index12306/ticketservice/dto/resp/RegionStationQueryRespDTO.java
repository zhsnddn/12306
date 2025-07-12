package com.zhsnddn.index12306.ticketservice.dto.resp;

import lombok.Data;

/**
 * 地区以及车站查询响应参数
 */
@Data
public class RegionStationQueryRespDTO {

    /**
     * 名称
     */
    private String name;

    /**
     * 地区编码
     */
    private String code;

    /**
     * 拼音
     */
    private String spell;
}
