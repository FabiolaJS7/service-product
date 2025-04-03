package com.bootcamp.service.product.service.impl;


import com.bootcamp.service.product.constants.ProductTypeConstants;
import com.bootcamp.service.product.model.*;
import com.bootcamp.service.product.service.BusinessManager;
import com.bootcamp.service.product.util.AuditDataUtil;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class BusinessManagerImpl implements BusinessManager {

    @Override
    public Product createProduct(ProductRequest prq) {
        Product product = new Product();
        DetailsProduct detailsProduct = new DetailsProduct();

        Customer customer = new Customer();

        //Identificar a qué familia pertenece ProductType : ACTIVE o PASSIVE
        if (ProductTypeConstants.PASSIVE_PRODUCTS.contains(prq.getProductType())) {
            detailsProduct.setFamilyProduct("PASSIVE");
            switch (prq.getProductType()) {
                case ProductTypeConstants.SAVING_ACCOUNT:
                    SavingAccount savingAccount = new SavingAccount();
                    savingAccount.setAccountNumber(UUID.randomUUID().toString());
                    detailsProduct.setPassiveProduct(savingAccount);
                    break;
                case ProductTypeConstants.CURRENT_ACCOUNT:
                    CurrentAccount currentAccount = new CurrentAccount();
                    currentAccount.setAccountNumber(UUID.randomUUID().toString());
                    detailsProduct.setPassiveProduct(currentAccount);

                    break;
                case ProductTypeConstants.FIXED_ACCOUNT:
                    FixedAccount fixedAccount = new FixedAccount();
                    fixedAccount.setAccountNumber(UUID.randomUUID().toString());
                    detailsProduct.setPassiveProduct(fixedAccount);
                    break;
            }



        } else if (ProductTypeConstants.ACTIVE_PRODUCTS.contains(prq.getProductType())) {
            detailsProduct.setFamilyProduct("ACTIVE");
            ActiveProduct activeProduct = new ActiveProduct();
            activeProduct.setProductType(prq.getProductType());
            detailsProduct.setActiveProduct(activeProduct);

        }
        product.setDetailsProduct(detailsProduct);
        product.setCustomer(customer);
        return product;
    }
}
