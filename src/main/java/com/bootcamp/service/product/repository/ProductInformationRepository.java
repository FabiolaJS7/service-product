package com.bootcamp.service.product.repository;

import com.bootcamp.service.product.model.ProductInformation;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductInformationRepository extends ReactiveMongoRepository<ProductInformation, String> {
}
