package com.zhsnddn.index12306.ticketservice.service;

import com.zhsnddn.index12306.ticketservice.dto.req.TicketPageQueryReqDTO;
import com.zhsnddn.index12306.ticketservice.dto.resp.TicketPageQueryRespDTO;

public interface TicketService {
    /**
     * 根据条件查询车票
     */
    TicketPageQueryRespDTO pageListTicketQueryV1(TicketPageQueryReqDTO requestParam);
}
