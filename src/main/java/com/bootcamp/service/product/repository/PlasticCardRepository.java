package com.bootcamp.service.product.repository;

import com.bootcamp.service.product.model.PlasticCard;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface PlasticCardRepository extends ReactiveCrudRepository<PlasticCard, Long> {
    Mono<PlasticCard> findPlasticCardById(String id);
}
