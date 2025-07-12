package com.zhsnddn.index12306.ticketservice.dto.resp;

import lombok.Data;
/**
 * 地区以及车站查询响应对象
 */
@Data
public class StationQueryRespDTO {

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

    /**
     * 城市名称
     */
    private String regionName;
}
