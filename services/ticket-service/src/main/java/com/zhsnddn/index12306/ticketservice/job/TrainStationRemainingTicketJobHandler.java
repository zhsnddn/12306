package com.zhsnddn.index12306.ticketservice.job;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.xxl.job.core.handler.annotation.XxlJob;
import com.zhsnddn.index12306.framework.starter.cache.DistributedCache;
import com.zhsnddn.index12306.ticketservice.dao.entity.TrainDO;
import com.zhsnddn.index12306.ticketservice.dao.entity.TrainStationRelationDO;
import com.zhsnddn.index12306.ticketservice.dao.mapper.TrainMapper;
import com.zhsnddn.index12306.ticketservice.dao.mapper.TrainStationRelationMapper;
import com.zhsnddn.index12306.ticketservice.job.base.AbstractTrainStationJobHandlerTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.zhsnddn.index12306.ticketservice.common.constant.Index12306Constant.ADVANCE_TICKET_DAY;
import static com.zhsnddn.index12306.ticketservice.common.constant.RedisKeyConstant.TRAIN_STATION_REMAINING_TICKET;


/**
 * 列车站点余票定时任务
 */
@Deprecated
@RestController
@RequiredArgsConstructor
public class TrainStationRemainingTicketJobHandler extends AbstractTrainStationJobHandlerTemplate {

    private final TrainStationRelationMapper trainStationRelationMapper;
    private final DistributedCache distributedCache;
    private final TrainMapper trainMapper;

    @XxlJob(value = "trainStationRemainingTicketJobHandler")
    @GetMapping("/api/ticket-service/train-station-remaining-ticket/job/cache-init/execute")
    @Override
    public void execute() {
        super.execute();
    }

    @Override
    protected void actualExecute(List<TrainDO> trainDOPageRecords) {
        for (TrainDO each : trainDOPageRecords) {
            LambdaQueryWrapper<TrainStationRelationDO> relationQueryWrapper = Wrappers.lambdaQuery(TrainStationRelationDO.class)
                    .eq(TrainStationRelationDO::getTrainId, each.getId());
            List<TrainStationRelationDO> trainStationRelationDOList = trainStationRelationMapper.selectList(relationQueryWrapper);
            if (CollUtil.isEmpty(trainStationRelationDOList)) {
                return;
            }
            for (TrainStationRelationDO item : trainStationRelationDOList) {
                Long trainId = item.getTrainId();
                TrainDO trainDO = trainMapper.selectById(trainId);
                Map<String, String> trainStationRemainingTicket = new HashMap<>();
                switch (trainDO.getTrainType()) {
                    case 0 -> {
                        trainStationRemainingTicket.put("0", "10");
                        trainStationRemainingTicket.put("1", "140");
                        trainStationRemainingTicket.put("2", "810");
                    }
                    case 1 -> {
                        trainStationRemainingTicket.put("3", "96");
                        trainStationRemainingTicket.put("4", "192");
                        trainStationRemainingTicket.put("5", "216");
                        trainStationRemainingTicket.put("13", "216");
                    }
                    case 2 -> {
                        trainStationRemainingTicket.put("6", "96");
                        trainStationRemainingTicket.put("7", "192");
                        trainStationRemainingTicket.put("8", "216");
                        trainStationRemainingTicket.put("13", "216");
                    }
                }
                StringRedisTemplate stringRedisTemplate = (StringRedisTemplate) distributedCache.getInstance();
                String buildCacheKey = TRAIN_STATION_REMAINING_TICKET + StrUtil.join("_", each.getId(), item.getDeparture(), item.getArrival());
                stringRedisTemplate.opsForHash().putAll(buildCacheKey, trainStationRemainingTicket);
                stringRedisTemplate.expire(buildCacheKey, ADVANCE_TICKET_DAY, TimeUnit.DAYS);
            }
        }
    }
}
