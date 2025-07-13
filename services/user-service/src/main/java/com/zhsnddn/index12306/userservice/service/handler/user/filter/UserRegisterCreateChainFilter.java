package com.zhsnddn.index12306.userservice.service.handler.user.filter;

import com.zhsnddn.index12306.framework.starter.designpattern.chain.AbstractChainHandler;
import com.zhsnddn.index12306.userservice.common.enums.UserChainMarkEnum;
import com.zhsnddn.index12306.userservice.dto.req.UserRegisterReqDTO;

/**
 * 用户注册责任链过滤器
 */
public interface UserRegisterCreateChainFilter<T extends UserRegisterReqDTO> extends AbstractChainHandler<UserRegisterReqDTO> {

    @Override
    default String mark() {
        return UserChainMarkEnum.USER_REGISTER_FILTER.name();
    }
}
