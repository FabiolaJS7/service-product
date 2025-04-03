package com.bootcamp.service.product.service.impl;

import com.bootcamp.service.product.model.*;
import com.bootcamp.service.product.repository.ProductRepository;
import com.bootcamp.service.product.service.BusinessManager;
import com.bootcamp.service.product.transfer.ProductTransfer;
import com.bootcamp.service.product.util.JsonTransferUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @InjectMocks
    ProductServiceImpl productService;

    @Mock
    ProductRepository productRepository;

    @Mock
    BusinessManager businessManager;
    
    @Mock
    ProductTransfer productTransfer;


    @Disabled
    @Test
    void testCreateProduct() {
        //Arrage , preparamos el escensario, los objetos simulados
        ProductRequest productRequest = getProductRequestMock();
        Product productDto = getProductMock();

        Mono<ProductRequest> productRequestMono = Mono.just(productRequest);

        Mockito.when(productRepository.save(productDto)).thenReturn(Mono.just(productDto));
        Mockito.when(businessManager.createProduct(productRequest)).thenReturn(productDto);
        //Act - llamamos al metodo que estamos probando
        final Mono<String> result = productService.createProduct(productRequestMono);

        //Assert para verificar la informacion que espero de la prueba
        Assertions.assertEquals(productDto.getId(), result.block());
        Mockito.verify(productRepository, Mockito.times(1)).save(productDto); //cuántas veces se desea que se llame al save

    }


    @Disabled
    @Test
    void testCreateProduct_shouldReturn404WhenSaveFails() {
        // Arrange
        ProductRequest productRequest = getProductRequestMock();
        Product productDto = getProductMock();

        Mono<ProductRequest> productRequestMono = Mono.just(productRequest);

        // Simula que el repositorio lanza un error al intentar guardar
        Mockito.when(productRepository.save(Mockito.any(Product.class)))
                .thenReturn(Mono.error(new RuntimeException("Save failed")));

        Mockito.when(businessManager.createProduct(productRequest)).thenReturn(productDto);

        // Act
        Mono<ResponseEntity<String>> result = productService.createProduct(productRequestMono)
                .map(ResponseEntity::ok)
                .onErrorResume(e -> Mono.just(ResponseEntity.status(404).body("Product creation failed")));

        // Assert
        ResponseEntity<String> response = result.block();
        Assertions.assertEquals(404, response.getStatusCodeValue());
        Assertions.assertEquals("Product creation failed", response.getBody());
    }

    @Test
    void testGettingProductResponses_whenProductHasStatusActive_shouldReturn200() {
        //Arrage , preparamos el escensario, los objetos simulados
        List<Product> products = Arrays.asList(JsonTransferUtil.getObjectFromJSONFile(Product[].class, "products.json"));
        System.out.println("products: " + JsonTransferUtil.objectToJson(products));
        List<ProductResponse> productResponses = products.stream().map(this::getProductResponseOfProduct)
                .toList();

        Mockito.when(productRepository.findAll()).thenReturn(Flux.fromIterable(products));
        Mockito.when(productTransfer.getProductResponseOfProduct(Mockito.any(Product.class)))
                .thenAnswer(invocation -> {
                    Product product = invocation.getArgument(0);
                    return this.getProductResponseOfProduct(product);
                });

        //Act - llamamos al metodo que estamos probando
        final Flux<ProductResponse> result = productService.findAllProducts();

        // Assert
        StepVerifier.create(result)
                .expectNextSequence(productResponses) // Verifica que los elementos coincidan
                .verifyComplete();

        // verificar que el Flux no sea nulo
        Assertions.assertNotNull(result);

    }

    private ProductResponse getProductResponseOfProduct(Product product) {
        ProductResponse productResponse = new ProductResponse();
        productResponse.setId(product.getId());
        productResponse.setFamily(product.getDetailsProduct().getFamilyProduct());

        CustomerBean customerBean = new CustomerBean();
        customerBean.setCustomerId(product.getCustomer().getCustomerId());
        customerBean.setCustomerType(product.getCustomer().getCustomerType());
        productResponse.setCustomer(customerBean);

        DetailsProduct detailsProduct = product.getDetailsProduct();

        if (productResponse.getFamily().equals("ACTIVE")) {
            ActiveProduct activeProduct = detailsProduct.getActiveProduct();

            ActiveProductBean activeProductBean = new ActiveProductBean();
            activeProductBean.setHasCreditCard(activeProduct.isHasCreditCard());
            activeProductBean.setCreditLimit(activeProduct.getCreditLimit());
            activeProductBean.setCreditLimitUsed(activeProduct.getCreditLimitUsed());
            productResponse.setActiveProduct(activeProductBean);
        } else {
            PassiveProduct passiveProduct = detailsProduct.getPassiveProduct();

            PassiveProductBean passiveProductBean = new PassiveProductBean();
            passiveProductBean.isFreeCommission(passiveProduct.isFreeCommission());
            passiveProductBean.setAmountOfOpen(passiveProduct.getAmountOfOpen());
            passiveProductBean.setAccountNumber(passiveProduct.getAccountNumber());

            InfoTransactionBean infoTransactionBean = new InfoTransactionBean();
            infoTransactionBean.setCommission(passiveProduct.getCommission());
            infoTransactionBean.setMaxPerMonth(passiveProduct.getMaxMovementPerMonth());
            infoTransactionBean.setTransactionDone(String.valueOf(passiveProduct.getTransactionDone()));
            infoTransactionBean.setEnabledToMovement(passiveProduct.isEnabledToMovement());
            passiveProductBean.setInforToTransaction(infoTransactionBean);

            productResponse.setPassiveProduct(passiveProductBean);
        }

        productResponse.setHolders(product.getHolders().stream().map(additionalPerson -> {
            AdditionalPersonBean additionalPersonBean = new AdditionalPersonBean();
            additionalPersonBean.setFullName(additionalPerson.getFullName());
            additionalPersonBean.setPhone(additionalPerson.getPhone());
            additionalPersonBean.setEmail(additionalPerson.getEmail());
            IdentificationBean identificationBean = new IdentificationBean();
            identificationBean.setTypeIdentification(additionalPerson.getIdentification().getTypeIdentification());
            identificationBean.setNumberIdentification(additionalPerson.getIdentification().getNumberIdentification());
            additionalPersonBean.setIdentification(identificationBean);
            return additionalPersonBean;
        }).toList());

        productResponse.setAuthorizedSignatories(product.getAuthorizedSignatories().stream().map(additionalPerson -> {
            AdditionalPersonBean additionalPersonBean = new AdditionalPersonBean();
            additionalPersonBean.setFullName(additionalPerson.getFullName());
            additionalPersonBean.setPhone(additionalPerson.getPhone());
            additionalPersonBean.setEmail(additionalPerson.getEmail());
            IdentificationBean identificationBean = new IdentificationBean();
            identificationBean.setTypeIdentification(additionalPerson.getIdentification().getTypeIdentification());
            identificationBean.setNumberIdentification(additionalPerson.getIdentification().getNumberIdentification());
            additionalPersonBean.setIdentification(identificationBean);
            return additionalPersonBean;
        }).toList());

        return productResponse;
    }


    private ProductRequest getProductRequestMock() {
        ProductRequest productRequest = new ProductRequest();
        productRequest.setProductType("SA");
        return productRequest;
    }

    private Product getProductMock() {
        Product productDto = new Product();
        productDto.setId("ID-PRUEBA");
        return productDto;
    }
}