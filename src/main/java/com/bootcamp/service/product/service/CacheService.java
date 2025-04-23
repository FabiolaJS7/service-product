package com.bootcamp.service.product.service;

import reactor.core.publisher.Mono;

public interface CacheService {

    Mono<Boolean> save(String key, Object value);
    Mono<Object> get(String key);
    Mono<Boolean> delete(String key);
}
