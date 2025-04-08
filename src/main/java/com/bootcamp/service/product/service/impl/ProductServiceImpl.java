package com.bootcamp.service.product.service.impl;


import com.bootcamp.service.product.constants.CasesUpdateConstants;
import com.bootcamp.service.product.model.*;
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

    @Override
    public Mono<String> createProduct(Mono<ProductRequest> productRequest) {
        return productRequest.flatMap(prq -> {
                    log.info("Product creating {}", JsonTransferUtil.objectToJson(prq));
                    return productRepository.save(productTransfer.getProductOfProductRequest(prq));
                })
                .doOnNext(product -> log.info("Product saved {}", JsonTransferUtil.objectToJson(product)))
                .map(Product::getId)
                .doOnError(e -> log.error("Error creating product: {}", e.getMessage(), e));

    }

    @Override
    public Flux<ProductResponse> findAllProducts() {
        return productRepository.findAll()
                .doOnSubscribe(subscription -> log.info("Products searching {}", JsonTransferUtil.objectToJson(subscription)))
                .map(product -> productTransfer.getProductResponseOfProduct(product))
                .doOnComplete(() -> log.info("Products found"))
                .doOnError(e -> log.error("Error fetching products: {}", e.getMessage(), e));
    }

    @Override
    public Mono<ProductResponse> updateProduct(String productId, Mono<ProductUpdateRQ> productUpdateRQ) {
        return productRepository.findById(productId)
                .doOnNext(product -> log.info("Product found to update {}", JsonTransferUtil.objectToJson(product)))
                .flatMap(product ->
                        productUpdateRQ.flatMap(proToUpdate -> {
                           this.buildProductToUpdate(proToUpdate, product);
                            return productRepository.save(product);
                        })
                        ).map(product -> productTransfer.getProductResponseOfProduct(product))
                .doOnNext(product -> log.info("Product updated {}", JsonTransferUtil.objectToJson(product)))
                .switchIfEmpty(Mono.just(new ProductResponse()))
                .doOnError(e -> log.error("Error updating product: {}", e.getMessage(), e));
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
                .doOnNext(product -> log.info("Product found to search for customer: {}", customerId))
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
                .flatMap(balanceBeanRequest1 -> productRepository.findById(productId)
                        .flatMap(product -> balanceTransfer.updateBalance(product, balanceBeanRequest1))
                        .flatMap(product -> productRepository.save(product)))
                .flatMap(product -> balanceTransfer.getBalanceOfProduct(product))
                .doOnSubscribe(subscription -> log.info("Updating balance with movement."))
                .doOnSuccess(product -> log.info("Movement on balance was updated {}", JsonTransferUtil.objectToJson(product)))
                .doOnError(throwable -> log.error("Error {}", throwable));

    }

    @Override
    public Mono<ProductResponse> findProductPassiveByAccountNumber(String accountNumber) {
        return productRepository.findProductsByDetailsProduct_PassiveProduct_AccountNumber(accountNumber)
                .doOnSubscribe(s -> log.info("Getting passive product by accountNumber {}", accountNumber))
                .doOnSuccess(product -> log.info("Success product by accountNumber {}", JsonTransferUtil.objectToJson(product)))
                .map(product -> productTransfer.getProductResponseOfProduct(product))
                .doOnError(e -> log.error("Error fetching passive product by account number: {}", e.getMessage(), e))
                .switchIfEmpty(Mono.just(new ProductResponse()));

    }

    private void buildProductToUpdate(ProductUpdateRQ proToUpdate, Product product) {
        //Actualizará atributos del product dependiendo de lo que indique proToUpdate.getActionToUpdate()
        switch (proToUpdate.getActionToUpdate()) {
            case CasesUpdateConstants.CHANGE_ACTIVE_PRODUCT:
                ActiveProductBean activeProductBean = proToUpdate.getActiveProduct();
                ActiveProduct activeProductModel = product.getDetailsProduct().getActiveProduct();

                activeProductModel.setHasCreditCard(activeProductBean.getHasCreditCard());
                activeProductModel.setCreditLimit(activeProductBean.getCreditLimit());
                activeProductModel.setCreditLimitUsed(activeProductBean.getCreditLimitUsed());
                break;
            case CasesUpdateConstants.CHANGE_PASSIVE_PRODUCT:
                PassiveProductBean passiveProductBean = proToUpdate.getPassiveProduct();
                PassiveProduct passiveProductModel = product.getDetailsProduct().getPassiveProduct();

                passiveProductModel.setFreeCommission(passiveProductBean.getIsFreeCommission());
                passiveProductModel.setAmountOfOpen(passiveProductBean.getAmountOfOpen());

                InfoTransactionBean infoTransactionBean = passiveProductBean.getInforToTransaction();
                passiveProductModel.setCommission(infoTransactionBean.getCommission());
                passiveProductModel.setMaxMovementPerMonth(infoTransactionBean.getMaxPerMonth());
                passiveProductModel.setTransactionDone(Integer.valueOf(infoTransactionBean.getTransactionDone()));
                passiveProductModel.setEnabledToMovement(infoTransactionBean.getEnabledToMovement());
                break;
            case CasesUpdateConstants.CHANGE_HOLDERS:

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

                break;
            //case CasesUpdateConstants.CHANGE_SIGNATURES:
            default:
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
                break;

        }
    }


}
