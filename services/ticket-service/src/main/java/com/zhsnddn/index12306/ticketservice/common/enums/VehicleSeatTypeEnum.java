package com.zhsnddn.index12306.ticketservice.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Objects;

/**
 * 交通工具座位类型
 */
@RequiredArgsConstructor
public enum VehicleSeatTypeEnum {

    /**
     * 商务座
     */
    BUSINESS_CLASS(0, "BUSINESS_CLASS", "商务座"),

    /**
     * 一等座
     */
    FIRST_CLASS(1, "FIRST_CLASS", "一等座"),

    /**
     * 二等座
     */
    SECOND_CLASS(2, "SECOND_CLASS", "二等座"),

    /**
     * 二等包座
     */
    SECOND_CLASS_CABIN_SEAT(3, "SECOND_CLASS_CABIN_SEAT", "二等包座"),

    /**
     * 一等卧
     */
    FIRST_SLEEPER(4, "FIRST_SLEEPER", "一等卧"),

    /**
     * 二等卧
     */
    SECOND_SLEEPER(5, "SECOND_SLEEPER", "二等卧"),

    /**
     * 软卧
     */
    SOFT_SLEEPER(6, "SOFT_SLEEPER", "软卧"),

    /**
     * 硬卧
     */
    HARD_SLEEPER(7, "HARD_SLEEPER", "硬卧"),

    /**
     * 硬座
     */
    HARD_SEAT(8, "HARD_SEAT", "硬座"),

    /**
     * 高级软卧
     */
    DELUXE_SOFT_SLEEPER(9, "DELUXE_SOFT_SLEEPER", "高级软卧"),

    /**
     * 动卧
     */
    DINING_CAR_SLEEPER(10, "DINING_CAR_SLEEPER", "动卧"),

    /**
     * 软座
     */
    SOFT_SEAT(11, "SOFT_SEAT", "软座"),

    /**
     * 特等座
     */
    FIRST_CLASS_SEAT(12, "FIRST_CLASS_SEAT", "特等座"),

    /**
     * 无座
     */
    NO_SEAT_SLEEPER(13, "NO_SEAT_SLEEPER", "无座"),

    /**
     * 其他
     */
    OTHER(14, "OTHER", "其他");

    @Getter
    private final Integer code;

    @Getter
    private final String name;

    @Getter
    private final String value;

    /**
     * 根据编码查找名称
     */
    public static String findNameByCode(Integer code) {
        return Arrays.stream(VehicleSeatTypeEnum.values())
                .filter(each -> Objects.equals(each.getCode(), code))
                .findFirst()
                .map(VehicleSeatTypeEnum::getName)
                .orElse(null);
    }

    /**
     * 根据编码查找值
     */
    public static String findValueByCode(Integer code) {
        return Arrays.stream(VehicleSeatTypeEnum.values())
                .filter(each -> Objects.equals(each.getCode(), code))
                .findFirst()
                .map(VehicleSeatTypeEnum::getValue)
                .orElse(null);
    }
}
