package com.bootcamp.service.product.service;

import com.bootcamp.service.product.model.ProductRequest;
import com.bootcamp.service.product.model.ProductResponse;
import com.bootcamp.service.product.model.ProductUpdateRQ;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductService {

    Mono<String> createProduct(Mono<ProductRequest> productRequest);
    Flux<ProductResponse> findAllProducts();
    Mono<ProductResponse> updateProduct(String productId, Mono<ProductUpdateRQ> productUpdateRQ);
    Mono<Void> deleteProduct(String productId);
    Flux<ProductResponse> findProductsByCustomerId(String customerId);


}
