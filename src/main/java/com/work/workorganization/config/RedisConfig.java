package com.work.workorganization.config;

import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import javax.annotation.Resource;
/**
  * -@Desc: Redis配置类
  * -@Author: zhouzhiqiang
  * -@Date: 2024/7/24 16:57
 **/
@Configuration
public class RedisConfig extends CachingConfigurerSupport {

    @Resource
    private RedisConnectionFactory connectionFactory;
    /*
        1.RedisConnectionFactory 是获取RedisConnection对象的，RedisConnection相当于jdbc中的连接对象Connection表示和
            Redis进行连接
     */
    @Bean
    public RedisTemplate<Object, Object> redisTemplate() {
        //创建Redis模板对象
        RedisTemplate<Object, Object> redisTemplate = new RedisTemplate<>();

        //默认的Key序列化器为：JdkSerializationRedisSerializer
        //StringRedisSerializer支持字符串类型的转化，而且默认使用UTF-8编码
        //下面代码的意思是使用StringRedisSerializer序列化器替换默认的Key序列化器JdkSerializationRedisSerializer
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        redisTemplate.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());

        redisTemplate.setConnectionFactory(connectionFactory);
        return redisTemplate;
    }

}