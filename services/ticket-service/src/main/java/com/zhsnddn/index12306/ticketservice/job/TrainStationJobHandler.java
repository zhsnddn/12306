package com.zhsnddn.index12306.ticketservice.job;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.xxl.job.core.handler.annotation.XxlJob;
import com.zhsnddn.index12306.framework.starter.cache.DistributedCache;
import com.zhsnddn.index12306.ticketservice.common.constant.Index12306Constant;
import com.zhsnddn.index12306.ticketservice.dao.entity.TrainDO;
import com.zhsnddn.index12306.ticketservice.dao.entity.TrainStationDO;
import com.zhsnddn.index12306.ticketservice.dao.mapper.TrainStationMapper;
import com.zhsnddn.index12306.ticketservice.job.base.AbstractTrainStationJobHandlerTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.zhsnddn.index12306.ticketservice.common.constant.RedisKeyConstant.TRAIN_STATION_STOPOVER_DETAIL;


/**
 * 列车路线信息定时任务
 */
@Deprecated
@RestController
@RequiredArgsConstructor
public class TrainStationJobHandler extends AbstractTrainStationJobHandlerTemplate {

    private final TrainStationMapper trainStationMapper;
    private final DistributedCache distributedCache;

    @XxlJob(value = "trainStationJobHandler")
    @GetMapping("/api/ticket-service/train-station/job/cache-init/execute")
    @Override
    public void execute() {
        super.execute();
    }

    @Override
    protected void actualExecute(List<TrainDO> trainDOPageRecords) {
        for (TrainDO each : trainDOPageRecords) {
            LambdaQueryWrapper<TrainStationDO> queryWrapper = Wrappers.lambdaQuery(TrainStationDO.class)
                    .eq(TrainStationDO::getTrainId, each.getId());
            List<TrainStationDO> trainStationDOList = trainStationMapper.selectList(queryWrapper);
            distributedCache.put(
                    TRAIN_STATION_STOPOVER_DETAIL + each.getId(),
                    JSON.toJSONString(trainStationDOList),
                    Index12306Constant.ADVANCE_TICKET_DAY,
                    TimeUnit.DAYS
            );
        }
    }
}
