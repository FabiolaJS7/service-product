package com.bootcamp.service.product.service;

import com.bootcamp.service.product.model.ProductInformation;
import reactor.core.publisher.Mono;

public interface ProductInformationService {

    Mono<ProductInformation> create(String id);
}
