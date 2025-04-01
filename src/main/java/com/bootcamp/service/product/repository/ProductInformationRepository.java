package com.bootcamp.service.product.repository;

import com.bootcamp.service.product.model.ProductInformation;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface ProductInformationRepository extends ReactiveMongoRepository<ProductInformation, String> {

    Mono<ProductInformation> findProductInformationByCustomerId(String customerId);
}
