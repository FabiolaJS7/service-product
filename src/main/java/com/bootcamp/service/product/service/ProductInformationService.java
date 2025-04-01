package com.bootcamp.service.product.service;

import com.bootcamp.service.product.model.ProductInformation;
import com.bootcamp.service.product.model.ProductRequest;
import reactor.core.publisher.Mono;

public interface ProductInformationService {

    Mono<String> createProductInformation(Mono<ProductRequest> productRequest);
    Mono<ProductInformation> getProductInformation(String productId);


}
