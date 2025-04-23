package com.bootcamp.service.product.service.impl;

import com.bootcamp.service.product.service.CacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class CacheServiceImpl implements CacheService {

    @Autowired
    private ReactiveRedisTemplate<String, Object> redisTemplate;

    @Override
    public Mono<Boolean> save(String key, Object value) {
        return redisTemplate.opsForValue().set(key, value);
    }

    @Override
    public Mono<Object> get(String key) {
        return redisTemplate.opsForValue().get(key)
                .switchIfEmpty(Mono.empty());
    }

    @Override
    public Mono<Boolean> delete(String key) {
        return redisTemplate.opsForValue().delete(key);
    }
}
