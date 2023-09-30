package com.hugo.config;

import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonRedisSerializer;
import com.alibaba.fastjson.support.spring.GenericFastJsonRedisSerializer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Slf4j
@Configuration
@EnableConfigurationProperties(CacheProperties.class)
public class RedisConfiguration {

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        log.info("开始创建redis模板类");

        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        // key的序列化
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        // value的序列化
        redisTemplate.setValueSerializer(new GenericFastJsonRedisSerializer());
        redisTemplate.setHashValueSerializer(new GenericFastJsonRedisSerializer());

        return redisTemplate;
    }

    /**
     * Redis CacheConfiguration
     */
    @Bean
    public RedisCacheConfiguration redisCacheConfiguration(CacheProperties cacheProperties) {

        return RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(cacheProperties.getRedis().getTimeToLive())
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericFastJsonRedisSerializer()));
    }

}
