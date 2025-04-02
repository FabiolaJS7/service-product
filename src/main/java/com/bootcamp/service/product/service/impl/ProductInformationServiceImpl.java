package com.bootcamp.service.product.service.impl;

import com.bootcamp.service.product.constants.ClientTypeConstants;
import com.bootcamp.service.product.constants.ProductTypeConstants;
import com.bootcamp.service.product.mapper.ProductInformationMapper;
import com.bootcamp.service.product.model.*;
import com.bootcamp.service.product.repository.ProductInformationRepository;
import com.bootcamp.service.product.service.ProductInformationService;
import com.bootcamp.service.product.service.ProductManager;
import com.bootcamp.service.product.util.AuditDataUtil;
import com.bootcamp.service.product.util.JsonTransferUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashMap;

@Service
@AllArgsConstructor
@Slf4j
public class ProductInformationServiceImpl implements ProductInformationService {

    ProductInformationRepository productInformationRepository;
    ProductManager productManager;
    ProductInformationMapper productInformationMapper;

    @Override
    public Mono<String> createProductInformation(Mono<ProductRequest> productRequest) {
        return productRequest.flatMap(request -> {

            // Building HashMap with main details
            HashMap<String, String> mainDetails = new HashMap<>();
            mainDetails.put("CUSTOMER_TYPE", request.getCustomerType());
            mainDetails.put("PRODUCT_TYPE", request.getProductType());
            mainDetails.put("FAMILY", ProductTypeConstants.PASSIVE_PRODUCTS.contains(request.getProductType()) ? "PASSIVE" : "ACTIVE");
            log.info("Main Details : {}", mainDetails);

            // Search productInformationBy CustomerId
            System.out.println("asdasd");
            return productInformationRepository.findProductInformationByCustomerId(request.getCustomerId())
                    .defaultIfEmpty(new ProductInformation())
                    .flatMap(productInformation -> {

                        if (!enabledToCreateProduct(productInformation, mainDetails)) {
                            log.warn("Product creation not enabled for customerId: {}", request.getCustomerId());
                            return Mono.error(new IllegalStateException("Product creation not enabled"));
                        }

                        // creating new product
                        productManager.createNewProduct(request, productInformation);
                        productInformation.setCustomerType(request.getCustomerType());
                        productInformation.setAuditData(AuditDataUtil.create(request.getUserBank()));

                        return productInformationRepository.save(productInformation);
                    })
                    .doOnNext(prod -> log.info("Product information processed: {}", JsonTransferUtil.objectToJson(prod)))
                    .map(ProductInformation::getId)
                    .doOnError(e -> log.error("Error creating product: {}", e.getMessage(), e));
        });

    }

    private boolean enabledToCreateProduct(ProductInformation productInformation,
                                           HashMap<String, String> mainDetails) {
        boolean enabled = true;

        if (mainDetails.get("CUSTOMER_TYPE").equalsIgnoreCase(ClientTypeConstants.PERSONAL)) {
            assert productInformation != null;
            boolean exists = productInformation.getActiveProduct()
                    .stream()
                    .anyMatch(activeProduct -> mainDetails.get("PRODUCT_TYPE")
                            .equalsIgnoreCase(ProductTypeConstants.CREDIT_PERSONAL) && activeProduct.getProductType()
                            .equals(ProductTypeConstants.CREDIT_PERSONAL));
            if (exists) {
                enabled = false;
            }

        }

        return enabled;
    }

    @Override
    public Flux<ProductResponse> getProducts() {
        return productInformationRepository.findAll()
                .doOnSubscribe(subscription -> log.info("Getting all products from the database"))
                .map(productInformation -> productInformationMapper.getProductResponseOfProductInformation(productInformation))
                .doOnComplete(() -> log.info("Completed fetching and mapping all customers"))
                .doOnError(e -> log.error("Error occurred while getting customers: {}", e.getMessage(), e));
    }

    @Override
    public Mono<Boolean> updateProduct(String productId, Mono<UpdateProductRequest> updateProductRequestMono) {
        return productInformationRepository.findById(productId)
                .doOnNext(productInformation -> log.info("Product found: {}", JsonTransferUtil.objectToJson(productInformation)))
                .flatMap(productInformation ->
                    updateProductRequestMono
                            .flatMap(updateProductRequest -> {


                                return productInformationRepository.save(productInformation);
                            })
                )
                .hasElement()
                .doOnNext(updated -> {
                    if (updated) {
                        log.info("Product with ID {} was successfully updated", productId);
                    } else {
                        log.warn("Product with ID {} was not found", productId);
                    }
                })
                .doOnError(e -> log.error("Error occurred while updating product with ID {}: {}", productId, e.getMessage(), e))
                .switchIfEmpty(Mono.error(new RuntimeException("Product not found")));
    }

    @Override
    public Mono<Boolean> deleteProduct(String productId) {
        return productInformationRepository.findById(productId)
                .doOnNext(product -> log.info("Product found with ID {}", productId))
                .flatMap(productFounded -> productInformationRepository.delete(productFounded)
                        .then(Mono.just(true)))
                .switchIfEmpty(Mono.just(false)) // Devuelve false si no se encuentra el producto
                .doOnError(e -> log.error("Error occurred while deleting product with ID {}: {}", productId, e.getMessage(), e));
    }

}
