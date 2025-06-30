package com.zhsnddn.index12306.userservice.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhsnddn.index12306.userservice.dao.entity.UserDO;

/**
 * 用户信息持久层
 * 公众号：马丁玩编程，回复：加群，添加马哥微信（备注：12306）获取项目资料
 */
public interface UserMapper extends BaseMapper<UserDO> {

    /**
     * 注销用户
     *
     * @param userDO 注销用户入参
     */
    void deletionUser(UserDO userDO);
}
