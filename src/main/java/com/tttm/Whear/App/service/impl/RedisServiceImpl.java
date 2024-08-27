package com.tttm.Whear.App.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tttm.Whear.App.service.RedisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@Slf4j
@RequiredArgsConstructor
public class RedisServiceImpl implements RedisService {

    private final RedisTemplate<String, Object> redisTemplate;

    private final ObjectMapper objectMapper;

    @Override
    public void setValue(String key, Object value) {
        try {
            String hashValue = objectMapper.writeValueAsString(value);
            redisTemplate.opsForValue().set(key, hashValue);
        } catch (Exception e) {
            log.error("Set value error: {}", e.getMessage());
        }
    }
    @Override
    public void setExpire(String key, int timeOut) {
        redisTemplate.expire(key, timeOut, TimeUnit.SECONDS);
    }
    @Override
    public <T> T getValue(String key, Class<T> classValue) {
        T target = null;
        try {
            Object temp = redisTemplate.opsForValue().get(key);
            if (temp != null) {
                target = objectMapper.readValue(temp.toString(), classValue);
            }
        } catch (Exception e) {
            log.error("Get value error: {}", e.getMessage());
        }
        return target;
    }

    @Override
    public <T> T getValue(String key, String childKey, Class<T> classValue) {
        T target = null;
        try {
            String hashKey = objectMapper.writeValueAsString(childKey);
            Object temp = redisTemplate.opsForHash().get(key, hashKey);
            if (temp != null) {
                target = objectMapper.readValue(temp.toString(), classValue);
            }
        } catch (Exception e) {
            log.error("Get value error: {}", e.getMessage());
        }
        return target;
    }

    @Override
    public void setExpire(final String key, Object value, Long expireTime) {
        try {
            ValueOperations<String, Object> operations = redisTemplate.opsForValue();
            operations.set(key, objectMapper.writeValueAsString(value));
            redisTemplate.expire(key, expireTime, TimeUnit.SECONDS);
        } catch (Exception e) {
            log.error("Set expire error: {}", e.getMessage());
        }
    }

    @Override
    public Boolean exist(String key) {
        try {
            return Boolean.TRUE.equals(redisTemplate.hasKey(key));
        } catch (Exception e) {
            log.error("Check exist error: {}", e.getMessage());
        }
        return null;
    }

    @Override
    public void deleteKey(String key) {
        try {
            redisTemplate.delete(key);
        } catch (Exception e) {
            log.error("Delete key error: {}", e.getMessage());
        }
    }

}
