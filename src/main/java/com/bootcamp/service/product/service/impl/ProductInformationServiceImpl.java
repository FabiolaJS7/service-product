package com.bootcamp.service.product.service.impl;

import com.bootcamp.service.product.constants.ProductTypeConstants;
import com.bootcamp.service.product.model.*;
import com.bootcamp.service.product.repository.ProductInformationRepository;
import com.bootcamp.service.product.service.ProductInformationService;
import com.bootcamp.service.product.util.AuditDataUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@AllArgsConstructor
@Slf4j
public class ProductInformationServiceImpl implements ProductInformationService {

    private final AuditDataUtil auditDataUtil;
    ProductInformationRepository productInformationRepository;

    @Override
    public Mono<String> createProductInformation(Mono<ProductRequest> productRequest) {
        return productRequest.flatMap(prq -> {
            ProductInformation productInformation = new ProductInformation();
            productInformation.setCustomerId(prq.getCustomerId());

            switch (prq.getProductType()) {
                case ProductTypeConstants.SAVING_ACCOUNT:
                    SavingAccount savingAccount = new SavingAccount();
                    savingAccount.setAccountNumber(UUID.randomUUID().toString());
                    productInformation.getPassiveProduct().add(savingAccount);
                    break;
                case ProductTypeConstants.CURRENT_ACCOUNT:
                    CurrentAccount currentAccount = new CurrentAccount();
                    currentAccount.setAccountNumber(UUID.randomUUID().toString());
                    productInformation.getPassiveProduct().add(currentAccount);
                    break;
                case ProductTypeConstants.FIXED_ACCOUNT:
                    FixedAccount fixedAccount = new FixedAccount();
                    fixedAccount.setAccountNumber(UUID.randomUUID().toString());
                    productInformation.getPassiveProduct().add(fixedAccount);
                    break;
                case ProductTypeConstants.CREDIT_PERSONAL:
                    ActiveProduct activeProduct = new ActiveProduct();
                    activeProduct.setAccountNumber(UUID.randomUUID().toString());
                    activeProduct.setProductType(ProductTypeConstants.CREDIT_PERSONAL);
                    productInformation.getActiveProduct().add(activeProduct);
                    break;
                case ProductTypeConstants.CREDIT_BUSINESS:
                    ActiveProduct activeProduct1 = new ActiveProduct();
                    activeProduct1.setAccountNumber(UUID.randomUUID().toString());
                    activeProduct1.setProductType(ProductTypeConstants.CREDIT_BUSINESS);
                    productInformation.getActiveProduct().add(activeProduct1);
                    break;
                default:
                    ActiveProduct activeProduct2 = new ActiveProduct();
                    activeProduct2.setAccountNumber(UUID.randomUUID().toString());
                    activeProduct2.setProductType(ProductTypeConstants.CREDIT_CARD);
                    productInformation.getActiveProduct().add(activeProduct2);
                    break;
            }
            auditDataUtil.create(prq.getUserBank());
            return productInformationRepository.save(productInformation);

        }).map(ProductInformation::getId);

    }

    @Override
    public Mono<ProductInformation> getProductInformation(String productId) {
        return productInformationRepository.findById(productId);
    }
}
