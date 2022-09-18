package org.idea.threadpool.monitor.console.config;

import org.idea.threadpool.monitor.report.redis.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author linhao
 * @Date created in 11:15 下午 2022/9/7
 */
@Configuration
@EnableConfigurationProperties(value = {RedisProperties.class})
public class WebInitConfig {

    @Bean
    public IRedisFactory redisFactory(RedisProperties redisProperties) {
        return new RedisFactoryImpl(redisProperties);
    }

    @Bean
    @ConditionalOnBean(IRedisFactory.class)
    public IRedisService redisService(IRedisFactory redisFactory) {
        return new RedisServiceImpl(redisFactory);
    }

}
