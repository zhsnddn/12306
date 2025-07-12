package com.zhsnddn.index12306.ticketservice.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.zhsnddn.index12306.framework.starter.common.enums.FlagEnum;
import com.zhsnddn.index12306.framework.starter.common.toolkit.BeanUtil;
import com.zhsnddn.index12306.framework.starter.convention.exception.ClientException;
import com.zhsnddn.index12306.ticketservice.common.enums.RegionStationQueryTypeEnum;
import com.zhsnddn.index12306.ticketservice.dao.entity.RegionDO;
import com.zhsnddn.index12306.ticketservice.dao.entity.StationDO;
import com.zhsnddn.index12306.ticketservice.dao.mapper.RegionMapper;
import com.zhsnddn.index12306.ticketservice.dao.mapper.StationMapper;
import com.zhsnddn.index12306.ticketservice.dto.req.RegionStationQueryReqDTO;
import com.zhsnddn.index12306.ticketservice.dto.resp.RegionStationQueryRespDTO;
import com.zhsnddn.index12306.ticketservice.dto.resp.StationQueryRespDTO;
import com.zhsnddn.index12306.ticketservice.service.RegionStationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 地区以及车站查询服务实现方法
 */
@Service
@RequiredArgsConstructor
public class RegionStationServiceImpl implements RegionStationService {

    private final StationMapper stationMapper;
    private final RegionMapper regionMapper;

    @Override
    public List<StationQueryRespDTO> listAllStation() {
        List<StationDO> stationDOList = stationMapper.selectList(null);
        return BeanUtil.convert(stationDOList, StationQueryRespDTO.class);
    }



    @Override
    public List<RegionStationQueryRespDTO> listRegionStation(RegionStationQueryReqDTO requestParam) {
        if(StrUtil.isNotBlank(requestParam.getName())) {
            LambdaQueryWrapper<StationDO> queryWrapper = Wrappers.lambdaQuery(StationDO.class)
                    .likeRight(StationDO::getName, requestParam.getName())
                    .or()
                    .likeRight(StationDO::getSpell, requestParam.getName());
            List<StationDO> stationDOList = stationMapper.selectList(queryWrapper);
            return BeanUtil.convert(stationDOList, RegionStationQueryRespDTO.class);
        }
        LambdaQueryWrapper<RegionDO> queryWrapper = switch (requestParam.getQueryType()) {
            case 0 -> Wrappers.lambdaQuery(RegionDO.class)
                    .eq(RegionDO::getPopularFlag, FlagEnum.TRUE.code());
            case 1 -> Wrappers.lambdaQuery(RegionDO.class)
                    .in(RegionDO::getInitial, RegionStationQueryTypeEnum.A_E.getSpells());
            case 2 -> Wrappers.lambdaQuery(RegionDO.class)
                    .in(RegionDO::getInitial, RegionStationQueryTypeEnum.F_J.getSpells());
            case 3 -> Wrappers.lambdaQuery(RegionDO.class)
                    .in(RegionDO::getInitial, RegionStationQueryTypeEnum.K_O.getSpells());
            case 4 -> Wrappers.lambdaQuery(RegionDO.class)
                    .in(RegionDO::getInitial, RegionStationQueryTypeEnum.P_T.getSpells());
            case 5 -> Wrappers.lambdaQuery(RegionDO.class)
                    .in(RegionDO::getInitial, RegionStationQueryTypeEnum.U_Z.getSpells());
            default -> throw new ClientException("查询失败，请检查查询参数是否正确");
        };
        List<RegionDO> regionDOList = regionMapper.selectList(queryWrapper);
        return BeanUtil.convert(regionDOList, RegionStationQueryRespDTO.class);
    }
}
