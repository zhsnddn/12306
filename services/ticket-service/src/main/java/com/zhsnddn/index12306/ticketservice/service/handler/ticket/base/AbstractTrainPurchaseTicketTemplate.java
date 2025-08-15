package com.zhsnddn.index12306.ticketservice.service.handler.ticket.base;

import com.zhsnddn.index12306.framework.starter.designpattern.strategy.AbstractExecuteStrategy;
import com.zhsnddn.index12306.ticketservice.service.handler.ticket.dto.SelectSeatDTO;
import com.zhsnddn.index12306.ticketservice.service.handler.ticket.dto.TrainPurchaseTicketRespDTO;
import org.springframework.boot.CommandLineRunner;

import java.util.List;

/**
 * 抽象高铁购票模板基础服务
 */
public abstract class AbstractTrainPurchaseTicketTemplate implements IPurchaseTicket, CommandLineRunner, AbstractExecuteStrategy<SelectSeatDTO, List<TrainPurchaseTicketRespDTO>> {

    /**
     * 选择座位
     *
     * @param requestParam 购票请求入参
     * @return 乘车人座位
     */
    protected abstract List<TrainPurchaseTicketRespDTO> selectSeats(SelectSeatDTO requestParam);
}
