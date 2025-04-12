package com.bootcamp.service.product.expose;

import com.bootcamp.service.product.api.ApiApiDelegate;
import com.bootcamp.service.product.model.*;
import com.bootcamp.service.product.service.ProductService;
import com.bootcamp.service.product.service.ProductTypeService;
import com.bootcamp.service.product.util.JsonTransferUtil;
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
    @Autowired
    ProductTypeService productTypeService;

    @Override
    public Mono<ResponseEntity<String>> createProduct(Mono<ProductRequest> productRequest,
                                                      ServerWebExchange exchange) {
        log.info("-> Init create Product");
        return productService.createProduct(productRequest)
                .map(ResponseEntity::ok)
                .doOnNext(product -> log.info("end create product"))
                .onErrorResume(e -> Mono.just(new ResponseEntity<>(HttpStatus.BAD_REQUEST)));
    }

    @Override
    public Mono<ResponseEntity<Flux<ProductResponse>>> findAll(ServerWebExchange exchange) {
        log.info("-> Find All Products");
        return Mono.just(ResponseEntity.ok(productService.findAllProducts()))
                .onErrorResume(e -> Mono.just(new ResponseEntity<>(HttpStatus.NOT_FOUND)));
    }

    @Override
    public Mono<ResponseEntity<ProductResponse>> udpate(String productId, Mono<ProductUpdateRQ> productUpdateRQ,
                                                        ServerWebExchange exchange) {

        return productUpdateRQ
                .doOnNext(p -> log.info("-> Update product: {}, {}", productId, JsonTransferUtil.objectToJson(p)))
                .flatMap(p -> productService.updateProduct(productId, Mono.just(p)))
                .map(ResponseEntity::ok)
                .onErrorResume(e -> Mono.just(new ResponseEntity<>(HttpStatus.NOT_FOUND)));
    }

    @Override
    public Mono<ResponseEntity<Void>> delete(String productId, ServerWebExchange exchange) {
        log.info("-> Delete Product");
        return productService.deleteProduct(productId)
                .map(ResponseEntity::ok)
                .onErrorResume(e -> Mono.just(new ResponseEntity<>(HttpStatus.NOT_FOUND)));
    }

    @Override
    public Mono<ResponseEntity<Flux<ProductResponse>>> getProductsByCustomerId(String customerId,
                                                                                ServerWebExchange exchange) {
        log.info("-> Init get products by customer Id");
        return Mono.just(ResponseEntity.ok(productService.findProductsByCustomerId(customerId)))
                .onErrorResume(e -> Mono.just(new ResponseEntity<>(HttpStatus.NOT_FOUND)));
    }

    @Override
    public Mono<ResponseEntity<ProductResponse>> getProductById(String productId,
                                                                 ServerWebExchange exchange) {
        log.info("-> Get Product by id ");
        return productService.findProductById(productId)
                .map(ResponseEntity::ok)
                .onErrorResume(e -> Mono.just(new ResponseEntity<>(HttpStatus.NOT_FOUND)));
    }

    @Override
    public Mono<ResponseEntity<BalanceBeanResponse>> getProductBalance(String productId,
                                                                        ServerWebExchange exchange) {

        log.info("-> Get Product Balance");
        return productService.findBalanceByProductId(productId)
                .map(ResponseEntity::ok)
                .onErrorResume(e -> Mono.just(new ResponseEntity<>(HttpStatus.NOT_FOUND)));

    }

    @Override
    public Mono<ResponseEntity<BalanceBeanResponse>> udpateBalance(String productId,
                                                                    Mono<BalanceBeanRequest> balanceBeanRequest,
                                                                    ServerWebExchange exchange) {
        return balanceBeanRequest
                .doOnNext(b -> log.info("Update balance of product {}", productId))
                .flatMap(b -> productService.updateBalance(productId, Mono.just(b)))
                .map(ResponseEntity::ok)
                .onErrorResume(e -> Mono.just(new ResponseEntity<>(HttpStatus.NOT_FOUND)));
    }

    @Override
    public Mono<ResponseEntity<ProductResponse>> getProductByAccountNumber(String accountNumber,
                                                                            ServerWebExchange exchange) {
        log.info("-> Get Product By Account Number");
        return productService.findProductPassiveByAccountNumber(accountNumber)
                .map(ResponseEntity::ok)
                .onErrorResume(e -> Mono.just(new ResponseEntity<>(HttpStatus.NOT_FOUND)));
    }

    @Override
    public Mono<ResponseEntity<CreateProductTypeResponse>> createProductType(Mono<CreateProductTypeRequest> create,
                                                                             ServerWebExchange exchange) {
        log.info("-> Init create product type.");
        return productTypeService.createProductType(create)
                .map(ResponseEntity::ok)
                .doOnSubscribe(s -> log.info("End create product type: {}", s))
                .onErrorResume(e -> Mono.just(new ResponseEntity<>(HttpStatus.BAD_REQUEST)));
    }


}
