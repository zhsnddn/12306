package com.zhsnddn.index12306.ticketservice.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.zhsnddn.index12306.ticketservice.dao.entity.*;
import com.zhsnddn.index12306.ticketservice.dao.mapper.*;
import com.zhsnddn.index12306.ticketservice.dto.domain.SeatClassDTO;
import com.zhsnddn.index12306.ticketservice.dto.domain.TicketListDTO;
import com.zhsnddn.index12306.ticketservice.dto.req.TicketPageQueryReqDTO;
import com.zhsnddn.index12306.ticketservice.dto.resp.TicketPageQueryRespDTO;
import com.zhsnddn.index12306.ticketservice.service.TicketService;
import com.zhsnddn.index12306.ticketservice.toolkit.DateUtil;
import com.zhsnddn.index12306.ticketservice.toolkit.TimeStringComparator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

import static cn.hutool.core.date.DateUtil.betweenDay;
import static com.zhsnddn.index12306.ticketservice.toolkit.DateUtil.convertDateToLocalTime;


@Service
@RequiredArgsConstructor
public class TicketServiceImpl implements TicketService {

    private final StationMapper stationMapper;
    private final TrainMapper trainMapper;
    private final SeatMapper seatMapper;
    private final TrainStationRelationMapper trainStationRelationMapper;
    private final TrainStationPriceMapper trainStationPriceMapper;

    @Override
    public TicketPageQueryRespDTO pageListTicketQueryV1(TicketPageQueryReqDTO requestParam) {

        //TODO:加缓存以及分布式锁
        //TODO:使用责任链过滤不合理数据
        //首先查询站点信息
        List<StationDO> stationDOList = stationMapper.selectList(Wrappers.emptyWrapper());
        Map<String, String> regionTrainStationMap = new HashMap<>();
        stationDOList.forEach(each -> regionTrainStationMap.put(each.getCode(), each.getRegionName()));
        List<Object> stationDetails = new ArrayList<>();
        stationDetails.add(regionTrainStationMap.get(requestParam.getFromStation()));
        stationDetails.add(regionTrainStationMap.get(requestParam.getToStation()));

        List<TicketListDTO> seatResults = new ArrayList<>();
        LambdaQueryWrapper<TrainStationRelationDO> queryWrapper = Wrappers.lambdaQuery(TrainStationRelationDO.class)
                .eq(TrainStationRelationDO::getStartRegion, stationDetails.get(0))
                .eq(TrainStationRelationDO::getEndRegion, stationDetails.get(1));
        List<TrainStationRelationDO> trainStationRelationList = trainStationRelationMapper.selectList(queryWrapper);
        for (TrainStationRelationDO each : trainStationRelationList) {
            TrainDO trainDO = trainMapper.selectById(each.getTrainId());
            TicketListDTO result = TicketListDTO.builder()
                    .trainId(String.valueOf(trainDO.getId()))
                    .trainNumber(trainDO.getTrainNumber())
                    .departureTime(convertDateToLocalTime(each.getDepartureTime(), "HH:mm"))
                    .arrivalTime(convertDateToLocalTime(each.getArrivalTime(), "HH:mm"))
                    .duration(DateUtil.calculateHourDifference(each.getDepartureTime(), each.getArrivalTime()))
                    .departure(each.getDeparture())
                    .arrival(each.getArrival())
                    .departureFlag(each.getDepartureFlag())
                    .arrivalFlag(each.getArrivalFlag())
                    .trainType(trainDO.getTrainType())
                    .trainBrand(trainDO.getTrainBrand())
                    .build();
            if (StrUtil.isNotBlank(trainDO.getTrainTag())) {
                result.setTrainTags(StrUtil.split(trainDO.getTrainTag(), ","));
            }
            long betweenDay = betweenDay(each.getDepartureTime(), each.getArrivalTime(), false);
            result.setDaysArrived((int) betweenDay);
            result.setSaleStatus(new Date().after(trainDO.getSaleTime()) ? 0 : 1);
            result.setSaleTime(convertDateToLocalTime(trainDO.getSaleTime(), "MM-dd HH:mm"));
            seatResults.add(result);
        }
        seatResults = seatResults.stream().sorted(new TimeStringComparator()).toList();
        for(TicketListDTO each : seatResults) {
            LambdaQueryWrapper<TrainStationPriceDO> trainStationPriceQueryWrapper = Wrappers.lambdaQuery(TrainStationPriceDO.class)
                    .eq(TrainStationPriceDO::getDeparture, each.getDeparture())
                    .eq(TrainStationPriceDO::getArrival, each.getArrival())
                    .eq(TrainStationPriceDO::getTrainId, each.getTrainId());
            List<TrainStationPriceDO> trainStationPriceDOList = trainStationPriceMapper.selectList(trainStationPriceQueryWrapper);
            List<SeatClassDTO> seatClassList = new ArrayList<>();
            trainStationPriceDOList.forEach(item -> {
                String seatType = String.valueOf(item.getSeatType());
                LambdaQueryWrapper<SeatDO> seatQuery = Wrappers.lambdaQuery(SeatDO.class)
                        .eq(SeatDO::getTrainId, each.getTrainId())
                        .eq(SeatDO::getSeatType, item.getSeatType())
                        .eq(SeatDO::getStartStation, item.getDeparture())
                        .eq(SeatDO::getEndStation, item.getArrival())
                        .eq(SeatDO::getSeatStatus, 0); // 假设 0 表示可用状态
                int quantity = Math.toIntExact(seatMapper.selectCount(seatQuery));
                seatClassList.add(new SeatClassDTO(item.getSeatType(),
                        quantity,
                        new BigDecimal(item.getPrice()).divide(new BigDecimal("100"), 1, RoundingMode.HALF_UP),
                        false));
            });
            each.setSeatClassList(seatClassList);
        }
        return TicketPageQueryRespDTO.builder()
                .trainList(seatResults)
                .departureStationList(buildDepartureStationList(seatResults))
                .arrivalStationList(buildArrivalStationList(seatResults))
                .trainBrandList(buildTrainBrandList(seatResults))
                .seatClassTypeList(buildSeatClassList(seatResults))
                .build();
    }

    private List<String> buildDepartureStationList(List<TicketListDTO> seatResults) {
        return seatResults.stream().map(TicketListDTO::getDeparture).distinct().collect(Collectors.toList());
    }

    private List<String> buildArrivalStationList(List<TicketListDTO> seatResults) {
        return seatResults.stream().map(TicketListDTO::getArrival).distinct().collect(Collectors.toList());
    }

    private List<Integer> buildSeatClassList(List<TicketListDTO> seatResults) {
        Set<Integer> resultSeatClassList = new HashSet<>();
        for (TicketListDTO each : seatResults) {
            for (SeatClassDTO item : each.getSeatClassList()) {
                resultSeatClassList.add(item.getType());
            }
        }
        return resultSeatClassList.stream().toList();
    }

    private List<Integer> buildTrainBrandList(List<TicketListDTO> seatResults) {
        Set<Integer> trainBrandSet = new HashSet<>();
        for (TicketListDTO each : seatResults) {
            if (StrUtil.isNotBlank(each.getTrainBrand())) {
                trainBrandSet.addAll(StrUtil.split(each.getTrainBrand(), ",").stream().map(Integer::parseInt).toList());
            }
        }
        return trainBrandSet.stream().toList();
    }
}
