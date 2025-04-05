package com.bootcamp.service.product.expose;

import com.bootcamp.service.product.api.ApiApiDelegate;
import com.bootcamp.service.product.model.ProductRequest;
import com.bootcamp.service.product.model.ProductResponse;
import com.bootcamp.service.product.model.ProductUpdateRQ;
import com.bootcamp.service.product.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class ServiceProductDelegateImpl implements ApiApiDelegate {

    @Autowired
    ProductService productService;

    @Override
    public Mono<ResponseEntity<String>> createProduct(Mono<ProductRequest> productRequest,
                                                      ServerWebExchange exchange) {
        log.info("-> Create Product");
        return productService.createProduct(productRequest)
                .map(ResponseEntity::ok)
                .onErrorResume(e -> Mono.just(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR)));
    }

    @Override
    public Mono<ResponseEntity<Flux<ProductResponse>>> findAll(ServerWebExchange exchange) {
        log.info("-> Find All Products");
        return Mono.just(ResponseEntity.ok(productService.findAllProducts()));
    }

    @Override
    public Mono<ResponseEntity<ProductResponse>> udpate(String productId, Mono<ProductUpdateRQ> productUpdateRQ,
                                                        ServerWebExchange exchange) {

        return productService.updateProduct(productId, productUpdateRQ)
                .map(ResponseEntity::ok)
                .onErrorResume(e -> Mono.just(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR)));
    }

    @Override
    public Mono<ResponseEntity<Void>> delete(String productId, ServerWebExchange exchange) {
        log.info("-> Delete Product");
        return productService.deleteProduct(productId)
                .map(ResponseEntity::ok)
                .onErrorResume(e -> Mono.just(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR)));
    }

    @Override
    public Mono<ResponseEntity<Flux<ProductResponse>>> getProductsByCustomerId(String customerId,
                                                                                ServerWebExchange exchange) {
        log.info("-> Get Products By Customer Id");
        return Mono.just(ResponseEntity.ok(productService.findProductsByCustomerId(customerId)));
    }

}
