package com.zhsnddn.index12306.userservice.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhsnddn.index12306.userservice.dao.entity.UserDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户信息持久层
 */
public interface UserMapper extends BaseMapper<UserDO> {

    /**
     * 注销用户
     *
     * @param userDO 注销用户入参
     */
    void deletionUser(UserDO userDO);
}
