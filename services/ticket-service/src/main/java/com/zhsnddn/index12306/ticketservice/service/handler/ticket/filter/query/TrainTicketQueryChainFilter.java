package com.zhsnddn.index12306.ticketservice.service.handler.ticket.filter.query;

import com.zhsnddn.index12306.framework.starter.designpattern.chain.AbstractChainHandler;
import com.zhsnddn.index12306.ticketservice.common.enums.TicketChainMarkEnum;
import com.zhsnddn.index12306.ticketservice.dto.req.TicketPageQueryReqDTO;
/**
 * 车票查询责任链过滤器
 */

public interface TrainTicketQueryChainFilter<T extends TicketPageQueryReqDTO> extends AbstractChainHandler<TicketPageQueryReqDTO> {

    @Override
    default String mark() {
        return TicketChainMarkEnum.TRAIN_QUERY_FILTER.name();
    }
}
