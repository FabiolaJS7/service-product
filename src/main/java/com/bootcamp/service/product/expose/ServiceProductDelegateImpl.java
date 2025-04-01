package com.bootcamp.service.product.expose;

import com.bootcamp.service.product.api.ApiApiDelegate;
import com.bootcamp.service.product.model.ProductRequest;
import com.bootcamp.service.product.model.ProductResponse;
import com.bootcamp.service.product.service.ProductInformationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class ServiceProductDelegateImpl implements ApiApiDelegate {

    @Autowired
    ProductInformationService productInformationService;

    @Override
    public Mono<ResponseEntity<String>> createProduct(Mono<ProductRequest> productRequest,
                                                       ServerWebExchange exchange) {
        log.info("-> Create Product");
        return productInformationService.createProductInformation(productRequest)
                .map(ResponseEntity::ok)
                .onErrorResume(e -> Mono.just(ResponseEntity.status(404).build()));
    }

    @Override
    public Mono<ResponseEntity<Flux<ProductResponse>>> findAll(ServerWebExchange exchange) {
        log.info("-> Find All Products");
        return Mono.just(ResponseEntity.ok(productInformationService.getProducts()));
    }




}
