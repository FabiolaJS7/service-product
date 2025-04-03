package com.bootcamp.service.product.expose;

import com.bootcamp.service.product.api.ApiApiDelegate;
import com.bootcamp.service.product.model.ProductRequest;
import com.bootcamp.service.product.model.ProductResponse;
import com.bootcamp.service.product.model.UpdateProductRequest;
import com.bootcamp.service.product.service.ProductService;
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
    ProductService productService;

    @Override
    public Mono<ResponseEntity<String>> createProduct(Mono<ProductRequest> productRequest,
                                                      ServerWebExchange exchange) {
        log.info("-> Create Product");
        return productService.createProductInformation(productRequest)
                .map(ResponseEntity::ok)
                .onErrorResume(e -> Mono.just(ResponseEntity.status(404).build()));
    }

   /*** @Override
    public Mono<ResponseEntity<Flux<ProductResponse>>> findAll(ServerWebExchange exchange) {
        log.info("-> Find All Products");
        return Mono.just(ResponseEntity.ok(productInformationService.getProducts()));
    }


    @Override
    public Mono<ResponseEntity<String>> udpate(String productId,
                                               Mono<UpdateProductRequest> updateProductRequest,
                                               ServerWebExchange exchange) {
        log.info("-> Udpate Product");
        return productInformationService.updateProduct(productId, updateProductRequest)
                .flatMap(aBoolean -> {
                    if (!aBoolean) {
                        log.warn("Product with ID {} not found", productId);
                        return Mono.just(ResponseEntity.status(404).body("false"));

                    }
                    return Mono.just(ResponseEntity.ok("true"));
                })
                .onErrorResume(e -> {
                    log.error("Error occurred while updating product with ID {}: {}", productId, e.getMessage(), e);
                    return Mono.just(ResponseEntity.status(500).body("false"));
                });

    }

    @Override
    public Mono<ResponseEntity<Void>> delete(String productId, ServerWebExchange exchange) {
        log.info("-> Delete Product");
        return productInformationService.deleteProduct(productId)
                .flatMap(deleted -> {
                    if (deleted) {
                        return Mono.just(ResponseEntity.ok().build());
                    } else {
                        return Mono.just(ResponseEntity.status(404).build());
                    }
                });
    }

***/


}
