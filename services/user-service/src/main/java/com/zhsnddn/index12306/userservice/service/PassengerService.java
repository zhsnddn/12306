package com.zhsnddn.index12306.userservice.service;

import com.zhsnddn.index12306.userservice.dto.req.PassengerRemoveReqDTO;
import com.zhsnddn.index12306.userservice.dto.req.PassengerReqDTO;
import com.zhsnddn.index12306.userservice.dto.req.PassengerUpdateReqDTO;
import com.zhsnddn.index12306.userservice.dto.resp.PassengerRespDTO;

import java.util.List;

/**
 * 乘车人接口层
 */
public interface PassengerService {

    /**
     * 根据用户名查询乘车人列表
     */
    List<PassengerRespDTO> listPassengerQueryByUsername(String username);

    /**
     * 新增乘车人
     */
    void savePassenger(PassengerReqDTO requestParam);

    /**
     * 删除乘车人
     */
    void removerPassenger(PassengerRemoveReqDTO requestParam);

    /**
     * 修改乘车人
     */
    void updatePassenger(PassengerUpdateReqDTO requestParam);
}
