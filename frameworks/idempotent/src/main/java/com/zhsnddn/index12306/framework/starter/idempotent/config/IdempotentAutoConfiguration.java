package com.zhsnddn.index12306.framework.starter.idempotent.config;

import com.zhsnddn.index12306.framework.starter.cache.DistributedCache;
import com.zhsnddn.index12306.framework.starter.idempotent.core.IdempotentAspect;
import com.zhsnddn.index12306.framework.starter.idempotent.core.param.IdempotentParamExecuteHandler;
import com.zhsnddn.index12306.framework.starter.idempotent.core.param.IdempotentParamService;
import com.zhsnddn.index12306.framework.starter.idempotent.core.spel.IdempotentSpELByMQExecuteHandler;
import com.zhsnddn.index12306.framework.starter.idempotent.core.spel.IdempotentSpELByRestAPIExecuteHandler;
import com.zhsnddn.index12306.framework.starter.idempotent.core.spel.IdempotentSpELService;
import com.zhsnddn.index12306.framework.starter.idempotent.core.token.IdempotentTokenController;
import com.zhsnddn.index12306.framework.starter.idempotent.core.token.IdempotentTokenExecuteHandler;
import com.zhsnddn.index12306.framework.starter.idempotent.core.token.IdempotentTokenService;
import org.redisson.api.RedissonClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * 幂等自动装配
 */
@EnableConfigurationProperties(IdempotentProperties.class)
public class IdempotentAutoConfiguration {

    /**
     * 幂等切面
     */
    @Bean
    public IdempotentAspect idempotentAspect() {
        return new IdempotentAspect();
    }

    /**
     * 参数方式幂等实现，基于 RestAPI 场景
     */
    @Bean
    @ConditionalOnMissingBean
    public IdempotentParamService idempotentParamExecuteHandler(RedissonClient redissonClient) {
        return new IdempotentParamExecuteHandler(redissonClient);
    }

    /**
     * Token 方式幂等实现，基于 RestAPI 场景
     */
    @Bean
    @ConditionalOnMissingBean
    public IdempotentTokenService idempotentTokenExecuteHandler(DistributedCache distributedCache,
                                                                IdempotentProperties idempotentProperties) {
        return new IdempotentTokenExecuteHandler(distributedCache, idempotentProperties);
    }

    /**
     * 申请幂等 Token 控制器，基于 RestAPI 场景
     */
    @Bean
    public IdempotentTokenController idempotentTokenController(IdempotentTokenService idempotentTokenService) {
        return new IdempotentTokenController(idempotentTokenService);
    }

    /**
     * SpEL 方式幂等实现，基于 RestAPI 场景
     */
    @Bean
    @ConditionalOnMissingBean
    public IdempotentSpELService idempotentSpELByRestAPIExecuteHandler(RedissonClient redissonClient) {
        return new IdempotentSpELByRestAPIExecuteHandler(redissonClient);
    }

    /**
     * SpEL 方式幂等实现，基于 MQ 场景
     */
    @Bean
    @ConditionalOnMissingBean
    public IdempotentSpELByMQExecuteHandler idempotentSpELByMQExecuteHandler(DistributedCache distributedCache) {
        return new IdempotentSpELByMQExecuteHandler(distributedCache);
    }
}
