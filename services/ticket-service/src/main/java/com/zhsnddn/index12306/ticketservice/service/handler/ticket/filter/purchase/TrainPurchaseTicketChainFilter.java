package com.zhsnddn.index12306.ticketservice.service.handler.ticket.filter.purchase;


import com.zhsnddn.index12306.framework.starter.designpattern.chain.AbstractChainHandler;
import com.zhsnddn.index12306.ticketservice.common.enums.TicketChainMarkEnum;
import com.zhsnddn.index12306.ticketservice.dto.req.PurchaseTicketReqDTO;

/**
 * 列车购买车票过滤器
 */
public interface TrainPurchaseTicketChainFilter<T extends PurchaseTicketReqDTO> extends AbstractChainHandler<PurchaseTicketReqDTO> {

    @Override
    default String mark() {
        return TicketChainMarkEnum.TRAIN_PURCHASE_TICKET_FILTER.name();
    }
}
