package com.bootcamp.service.product.service;

import com.bootcamp.service.product.model.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductService {

    Mono<String> createProduct(Mono<ProductRequest> productRequest);
    Flux<ProductResponse> findAllProducts();
    Mono<ProductResponse> updateProduct(String productId, Mono<ProductUpdateRQ> productUpdateRQ);
    Mono<Void> deleteProduct(String productId);
    Flux<ProductResponse> findProductsByCustomerId(String customerId);
    Mono<ProductResponse> findProductById(String productId);
    Mono<BalanceBeanResponse> findBalanceByProductId(String productId);
    Mono<BalanceBeanResponse> updateBalance(String productId, Mono<BalanceBeanRequest> balanceBeanRequest);


}
