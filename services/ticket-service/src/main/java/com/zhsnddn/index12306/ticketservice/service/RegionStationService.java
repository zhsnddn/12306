package com.zhsnddn.index12306.ticketservice.service;

import com.zhsnddn.index12306.ticketservice.dto.req.RegionStationQueryReqDTO;
import com.zhsnddn.index12306.ticketservice.dto.resp.RegionStationQueryRespDTO;
import com.zhsnddn.index12306.ticketservice.dto.resp.StationQueryRespDTO;

import java.util.List;

public interface RegionStationService {

    /**
     * 查询所有车站站点集合信息
     */
    List<StationQueryRespDTO> listAllStation();

    /**
     * 查询所有车站&城市站点集合信息
     */
    List<RegionStationQueryRespDTO> listRegionStation(RegionStationQueryReqDTO requestParam);
}
