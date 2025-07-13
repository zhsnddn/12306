package com.zhsnddn.index12306.userservice.service.handler.user.filter;

import com.zhsnddn.index12306.framework.starter.convention.exception.ClientException;
import com.zhsnddn.index12306.userservice.dto.req.UserRegisterReqDTO;
import com.zhsnddn.index12306.userservice.service.UserService;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;

/**
 * 用户注册检查证件号是否多次注销
 */
@Component
@RequiredArgsConstructor
public final class UserRegisterCheckDeletionChainHandler implements UserRegisterCreateChainFilter<UserRegisterReqDTO> {

    private final UserService userService;

    @Override
    public void handler(UserRegisterReqDTO requestParam) {
        Integer userDeletionNum = userService.queryUserDeletionNum(requestParam.getIdType(), requestParam.getIdCard());
        if (userDeletionNum >= 5) {
            throw new ClientException("证件号多次注销账号已被加入黑名单");
        }
    }

    @Override
    public int getOrder() {
        return 2;
    }
}
