package com.bootcamp.service.product.service;

import com.bootcamp.service.product.model.ProductRequest;
import com.bootcamp.service.product.model.ProductResponse;
import com.bootcamp.service.product.model.UpdateProductRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductInformationService {

    Mono<String> createProductInformation(Mono<ProductRequest> productRequest);
    Flux<ProductResponse> getProducts();
    Mono<Boolean> updateProduct(String productId, Mono<UpdateProductRequest> updateProductRequestMono);
    Mono<Boolean> deleteProduct(String productId);


}
