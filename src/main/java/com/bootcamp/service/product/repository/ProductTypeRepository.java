package com.bootcamp.service.product.repository;

import com.bootcamp.service.product.model.ProductType;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface ProductTypeRepository extends ReactiveMongoRepository<ProductType, String> {

    Mono<ProductType> findProductTypeByCode(String productTypeId);
}
