package com.zhsnddn.index12306.userservice.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.zhsnddn.index12306.framework.starter.database.base.BaseDO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *  用户复用表实体
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("t_user_reuse")
public final class UserReuseDO extends BaseDO {

    /**
     * 用户名
     */
    private String username;
}