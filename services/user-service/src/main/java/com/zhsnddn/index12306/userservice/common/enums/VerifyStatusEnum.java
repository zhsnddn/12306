package com.zhsnddn.index12306.userservice.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
/**
 * 审核状态枚举
 */

@AllArgsConstructor
public enum VerifyStatusEnum {

    /**
     * 未审核
     */
    UNREVIEWED(0),

    /**
     * 已审核
     */
    REVIEWED(1);

    @Getter
    private final int code;
}
