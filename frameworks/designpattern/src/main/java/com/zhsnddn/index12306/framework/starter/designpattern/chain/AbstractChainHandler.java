package com.zhsnddn.index12306.framework.starter.designpattern.chain;

import org.springframework.core.Ordered;

/**
 * 责任链处理器
 */
public interface AbstractChainHandler<T> extends Ordered {

    /**
     * 执行责任链逻辑
     *
     * @param requestParam 责任链执行入参
     */
    void handler(T requestParam);

    /**
     * @return 责任链组件标识
     */
    String mark();
}