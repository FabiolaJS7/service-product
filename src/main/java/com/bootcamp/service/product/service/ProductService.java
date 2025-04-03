package com.bootcamp.service.product.service;

import com.bootcamp.service.product.model.ProductRequest;
import reactor.core.publisher.Mono;

public interface ProductService {

    Mono<String> createProductInformation(Mono<ProductRequest> productRequest);


}
