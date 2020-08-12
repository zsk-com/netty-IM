package com.im.util;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * redis工具类 ,用于操作redis
 */
@Component
public class RedisUtil {
    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    public Object get(String key){
       if (!redisTemplate.hasKey(key)){
           return null;
       }
       return redisTemplate.opsForValue().get(key);
    }
}
