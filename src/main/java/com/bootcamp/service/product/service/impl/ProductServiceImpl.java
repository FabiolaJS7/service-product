package com.bootcamp.service.product.service.impl;


import com.bootcamp.service.product.constants.ActionUpdateConstants;
import com.bootcamp.service.product.constants.CasesUpdateConstants;
import com.bootcamp.service.product.model.*;
import com.bootcamp.service.product.transfer.ProductTransfer;
import com.bootcamp.service.product.repository.ProductRepository;
import com.bootcamp.service.product.service.BusinessManager;
import com.bootcamp.service.product.service.ProductService;
import com.bootcamp.service.product.util.JsonTransferUtil;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ServerWebExchange;
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
                .doOnSubscribe(subscription -> log.info("Products searching"))
                .map(product -> productTransfer.getProductResponseOfProduct(product))
                .doOnComplete(() -> log.info("Products found"))
                .doOnError(e -> log.error("Error fetching products: {}", e.getMessage(), e));
    }

    @Override
    public Mono<ProductResponse> updateProduct(String productId, Mono<ProductUpdateRQ> productUpdateRQ) {
        return productRepository.findById(productId)
                .doOnNext(product -> log.info("Product found {}", JsonTransferUtil.objectToJson(product)))
                .flatMap(product ->
                        productUpdateRQ.flatMap(proToUpdate -> {
                            //Actualizará atributos del product dependiendo de lo que indique proToUpdate.getActionToUpdate()
                            switch (proToUpdate.getActionToUpdate()){
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

                                    product.getHolders().clear();
                                    product.setHolders(holders);
                                    break;
                                    case CasesUpdateConstants.CHANGE_SIGNATURES:
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

                                        product.getAuthorizedSignatories().clear();
                                        product.setAuthorizedSignatories(signatures);
                                        break;

                            }
                            return productRepository.save(product);
                        })
                        ).map(product -> productTransfer.getProductResponseOfProduct(product))
                .doOnNext(product -> log.info("Product updated {}", JsonTransferUtil.objectToJson(product)))
                .switchIfEmpty(Mono.just(new ProductResponse()))
                .doOnError(e -> log.error("Error updating product: {}", e.getMessage(), e));
    }


}
