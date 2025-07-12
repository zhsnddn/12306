package com.zhsnddn.index12306.ticketservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.zhsnddn.index12306.framework.starter.common.toolkit.BeanUtil;
import com.zhsnddn.index12306.ticketservice.dao.entity.TrainStationDO;
import com.zhsnddn.index12306.ticketservice.dao.mapper.TrainStationMapper;
import com.zhsnddn.index12306.ticketservice.dto.resp.TrainStationQueryRespDTO;
import com.zhsnddn.index12306.ticketservice.service.TrainStationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 列车站点服务实现类
 */
@Service
@RequiredArgsConstructor
public class TrainStationServiceImpl implements TrainStationService {

    private final TrainStationMapper trainStationMapper;

    @Override
    public List<TrainStationQueryRespDTO> listTrainStationQuery(String trainId) {
        LambdaQueryWrapper<TrainStationDO> queryWrapper = Wrappers.lambdaQuery(TrainStationDO.class)
                .eq(TrainStationDO::getTrainId, trainId);
        List<TrainStationDO> trainStationDOList = trainStationMapper.selectList(queryWrapper);
        return BeanUtil.convert(trainStationDOList, TrainStationQueryRespDTO.class);
    }
}
