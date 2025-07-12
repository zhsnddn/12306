package com.zhsnddn.index12306.ticketservice.service;

import com.zhsnddn.index12306.ticketservice.dto.resp.TrainStationQueryRespDTO;

import java.util.List;

public interface TrainStationService {

    /**
     * 根据列车 ID 查询站点信息
     * @param trainId 列车 ID
     * @return 站点信息列表
     */
    List<TrainStationQueryRespDTO> listTrainStationQuery(String trainId);
}
