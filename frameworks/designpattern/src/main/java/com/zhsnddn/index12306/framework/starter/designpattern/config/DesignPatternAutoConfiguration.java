package com.zhsnddn.index12306.framework.starter.designpattern.config;

import com.zhsnddn.index12306.framework.starter.bases.config.ApplicationBaseAutoConfiguration;
import com.zhsnddn.index12306.framework.starter.designpattern.chain.AbstractChainContext;
import com.zhsnddn.index12306.framework.starter.designpattern.strategy.AbstractStrategyChoose;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.context.annotation.Bean;
/**
 * 设计模式自动配置
 **/
@ImportAutoConfiguration(ApplicationBaseAutoConfiguration.class)
public class DesignPatternAutoConfiguration {

    /**
     * 策略模式选择器
     */
    @Bean
    public AbstractStrategyChoose abstractStrategyChoose() {
        return new AbstractStrategyChoose();
    }

    /**
     * 责任链上下文
     */
    @Bean
    public AbstractChainContext abstractChainContext() {
        return new AbstractChainContext();
    }
}
