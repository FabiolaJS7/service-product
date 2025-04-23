package com.bootcamp.service.product.service.impl;


import com.bootcamp.service.product.constants.CasesUpdateConstants;
import com.bootcamp.service.product.model.*;
import com.bootcamp.service.product.service.CacheService;
import com.bootcamp.service.product.service.PlasticCardService;
import com.bootcamp.service.product.service.ProductTypeService;
import com.bootcamp.service.product.transfer.BalanceTransfer;
import com.bootcamp.service.product.transfer.ProductTransfer;
import com.bootcamp.service.product.repository.ProductRepository;
import com.bootcamp.service.product.service.ProductService;
import com.bootcamp.service.product.util.JsonTransferUtil;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;


@Service
@Slf4j
public class ProductServiceImpl implements ProductService {

    @Autowired
    ProductRepository productRepository;
    @Autowired
    ProductTransfer productTransfer;
    @Autowired
    BalanceTransfer balanceTransfer;
    @Autowired
    PlasticCardService plasticCardService;
    @Autowired
    CacheService cacheService;

    @Override
    public Mono<String> createProduct(Mono<ProductRequest> productRequest) {
        return productRequest
                .flatMap(prq -> {
                    log.info("Creating product {}", JsonTransferUtil.objectToJson(prq));
                    return productRepository.save(productTransfer.getProductOfProductRequest(prq));
                })
                .flatMap(product -> {
                    if (Boolean.TRUE.equals(product.getHasPlasticCard())) {
                        return plasticCardService.createPlasticCard(product).thenReturn(product);
                    } else {
                        return Mono.just(product);
                    }
                })
                .doOnNext(product -> log.info("Product saved {}", JsonTransferUtil.objectToJson(product)))
                .map(Product::getId)
                .doOnError(e -> log.error("Error creating product: {}", e.getMessage(), e));

    }

    @Override
    public Flux<ProductResponse> findAllProducts() {
        String cacheKey = "allProducts";

        // Intentar obtener los productos de redis
        return cacheService.get(cacheKey)
                .flatMapMany(cachedTransactions -> {
                    // Si los productos están en el caché, devolverlas
                    log.info("Transactions retrieved from cache {}", JsonTransferUtil.objectToJson(cachedTransactions));
                    return Flux.fromIterable((List<ProductResponse>) cachedTransactions);
                })
                .switchIfEmpty(
                        productRepository.findAll()
                                .doOnSubscribe(subscription -> log.info("Start getting products from database."))
                                .map(product -> productTransfer.getProductResponseOfProduct(product))
                                .collectList()
                                .flatMapMany(products -> {
                                    return cacheService.save(cacheKey, products)
                                            .thenMany(Flux.fromIterable(products));
                                })
                )
                .doOnComplete(() -> log.info("Products found"))
                .doOnError(e -> log.error("Error fetching products: {}", e.getMessage(), e));
    }

    @Override
    public Mono<ProductResponse> updateProduct(String productId, Mono<ProductUpdateRQ> productUpdateRQ) {
        return productRepository.findById(productId)
                .doOnNext(product -> log.info("Product found to update {}", JsonTransferUtil.objectToJson(product)))
                .flatMap(product ->
                        productUpdateRQ.flatMap(proToUpdate -> {

                            if (proToUpdate.getActionToUpdate().equalsIgnoreCase(CasesUpdateConstants.CREATE_DEBIT_CARD)) {
                                log.info("Updating product creating debit card.");
                                return plasticCardService.createPlasticCard(product)
                                        .flatMap(s -> {
                                            product.setPlasticCardId(s);
                                            product.setHasPlasticCard(true);
                                            return productRepository.save(product);
                                        });
                            } else if (proToUpdate.getActionToUpdate().equalsIgnoreCase(CasesUpdateConstants.CHANGE_HOLDERS)
                                    || proToUpdate.getActionToUpdate().equalsIgnoreCase(CasesUpdateConstants.CHANGE_SIGNATURES)) {
                                log.info("Updating product changing holders or signatures.");
                                buildProductToUpdate(proToUpdate, product);
                                return productRepository.save(product);
                            } else if (proToUpdate.getActionToUpdate().equalsIgnoreCase(CasesUpdateConstants.CARD_TO_ALL_ACCOUNTS)) {
                                log.info("Updating product associating plastic card all accounts. {}", JsonTransferUtil.objectToJson(product));
                                product.setHasPlasticCard(proToUpdate.getHasPlasticCard());
                                product.setPlasticCardId(proToUpdate.getPlasticCardId());
                                return productRepository.save(product);
                            }
                            return Mono.just(product);
                        })
                ).map(product -> productTransfer.getProductResponseOfProduct(product))
                .doOnNext(product -> log.info("Product updated {}", JsonTransferUtil.objectToJson(product)))
                .switchIfEmpty(Mono.just(new ProductResponse()))
                .doOnError(e -> log.error("Error updating product: {}", e.getMessage(), e));
    }

    private void buildProductToUpdate(ProductUpdateRQ proToUpdate, Product product) {

        if (proToUpdate.getActionToUpdate().equalsIgnoreCase(CasesUpdateConstants.CHANGE_HOLDERS)) {
            List<AdditionalPerson> holders = proToUpdate.getHolders()
                    .stream()
                    .map(additionalPersonBean -> {
                        AdditionalPerson additionalPerson = new AdditionalPerson();
                        additionalPerson.setFullName(additionalPersonBean.getFullName());
                        additionalPerson.setEmail(additionalPersonBean.getEmail());
                        additionalPerson.setPhone(additionalPersonBean.getPhone());

                        Identification identification = new Identification();
                        identification.setNumberIdentification(additionalPersonBean.getIdentification().getNumberIdentification());
                        identification.setTypeIdentification(additionalPersonBean.getIdentification().getTypeIdentification());
                        additionalPerson.setIdentification(identification);
                        return additionalPerson;
                    }).toList();

            if (product.getHolders() != null) {
                product.getHolders().addAll(holders);
            } else {
                product.setHolders(holders);
            }
        } else {
            List<AdditionalPerson> signatures = proToUpdate.getAuthorizedSignatories()
                    .stream()
                    .map(additionalPersonBean -> {
                        AdditionalPerson additionalPerson = new AdditionalPerson();
                        additionalPerson.setFullName(additionalPersonBean.getFullName());
                        additionalPerson.setEmail(additionalPersonBean.getEmail());
                        additionalPerson.setPhone(additionalPersonBean.getPhone());

                        Identification identification = new Identification();
                        identification.setNumberIdentification(additionalPersonBean.getIdentification().getNumberIdentification());
                        identification.setTypeIdentification(additionalPersonBean.getIdentification().getTypeIdentification());
                        additionalPerson.setIdentification(identification);
                        return additionalPerson;
                    }).toList();

            if (product.getAuthorizedSignatories() != null) {
                product.getAuthorizedSignatories().addAll(signatures);
            } else {
                product.setAuthorizedSignatories(signatures);
            }
        }

    }

    @Override
    public Mono<Void> deleteProduct(String productId) {
        return productRepository.findById(productId)
                .doOnNext(product -> log.info("Product found to delete {}", JsonTransferUtil.objectToJson(product)))
                .flatMap(product -> productRepository.delete(product))
                .doOnSuccess(product -> log.info("Product {} deleted", productId))
                .doOnError(e -> log.error("Error occurred while deleting product with ID {}: {}", productId, e.getMessage(), e));
    }

    @Override
    public Flux<ProductResponse> findProductsByCustomerId(String customerId) {
        return productRepository.findProductsByCustomer_CustomerId(customerId)
                .doOnSubscribe(subscription -> log.info("Searching products of customer: {}", customerId))
                .map(product -> productTransfer.getProductResponseOfProduct(product))
                .doOnNext(product -> log.info("Products found for customer: {}", customerId))
                .doOnError(e -> log.error("Error fetching products: {}", e.getMessage(), e));

    }

    @Override
    public Mono<ProductResponse> findProductById(String productId) {
        return productRepository.findById(productId)
                .doOnSubscribe(subscription -> log.info("Getting product by id"))
                .doOnSuccess(product -> log.info("Success product by id {}", JsonTransferUtil.objectToJson(product)))
                .map(product -> productTransfer.getProductResponseOfProduct(product))
                .doOnError(e -> log.error("Error fetching product: {}", e.getMessage(), e))
                .switchIfEmpty(Mono.just(new ProductResponse()));
    }

    @Override
    public Mono<BalanceBeanResponse> findBalanceByProductId(String productId) {
        return productRepository.findById(productId)
                .doOnSubscribe(subscription -> log.info("Searching product balance of customer: {}", productId))
                .flatMap(product -> balanceTransfer.getBalanceOfProduct(product))
                .doOnSubscribe(subscription -> log.info("Getting balance of product: {}", productId))
                .doOnSuccess(product -> log.info("Success getting balance of product: {}",
                        JsonTransferUtil.objectToJson(product)))
                .doOnError(e -> log.error("Error fetching balance of product: {}", productId, e))
                .switchIfEmpty(Mono.just(new BalanceBeanResponse()));
    }

    @Override
    public Mono<BalanceBeanResponse> updateBalance(String productId, Mono<BalanceBeanRequest> balanceBeanRequest) {
        return balanceBeanRequest
                .doOnSubscribe(subscription -> log.info("Updating balance of product {}", productId))
                .flatMap(balanceRequest -> productRepository.findById(productId)
                        .flatMap(product -> balanceTransfer.updateBalance(product, balanceRequest))
                        .flatMap(product -> productRepository.save(product)))
                .flatMap(product -> balanceTransfer.getBalanceOfProduct(product))
                .doOnSubscribe(subscription -> log.info("Updating balance with movement."))
                .doOnSuccess(product -> log.info("Movement on balance was updated {}", JsonTransferUtil.objectToJson(product)))
                .doOnError(throwable -> log.error("Error {}", throwable));

    }

    @Override
    public Mono<ProductResponse> findProductPassiveByAccountNumber(String accountNumber) {
        return productRepository.findProductsByAccountNumber(accountNumber)
                .doOnSubscribe(s -> log.info("Getting passive product by accountNumber {}", accountNumber))
                .doOnSuccess(product -> log.info("Success product by accountNumber {}", JsonTransferUtil.objectToJson(product)))
                .map(product -> productTransfer.getProductResponseOfProduct(product))
                .doOnError(e -> log.error("Error fetching passive product by account number: {}", e.getMessage(), e))
                .switchIfEmpty(Mono.just(new ProductResponse()));

    }
}
