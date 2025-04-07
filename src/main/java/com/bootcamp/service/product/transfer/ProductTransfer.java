package com.bootcamp.service.product.transfer;

import com.bootcamp.service.product.model.*;
import com.bootcamp.service.product.util.AuditDataUtil;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProductTransfer {


    public Product getProductOfProductRequest(ProductRequest prq) {
        Product product = new Product();
        product.setCustomer(getCustomerOfCustomerBean(prq.getCustomer()));
        product.setProductType(prq.getProductType());

        DetailsProduct detailsProduct = new DetailsProduct();
        if (prq.getActiveProduct() != null) {
            detailsProduct.setFamilyProduct("ACTIVE");

            ActiveProduct activeProduct = getActiveProduct(prq);
            detailsProduct.setActiveProduct(activeProduct);
            product.setDetailsProduct(detailsProduct);
        }

        if (prq.getPassiveProduct() != null) {
            detailsProduct.setFamilyProduct("PASSIVE");

            PassiveProduct passiveProduct = getPassiveProduct(prq);
            detailsProduct.setPassiveProduct(passiveProduct);
            product.setDetailsProduct(detailsProduct);
        }

        product.setHolders(getAdditionalPerson(prq.getHolders()));
        product.setAuthorizedSignatories(getAdditionalPerson(prq.getHolders()));
        product.setAuditData(AuditDataUtil.create(prq.getUserBank()));
        return product;
    }

    public ProductResponse getProductResponseOfProduct(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null");
        }
        ProductResponse productResponse = new ProductResponse();
        productResponse.setId(product.getId());
        productResponse.setFamily(product.getDetailsProduct().getFamilyProduct());
        productResponse.setProductType(product.getProductType());

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

    private List<AdditionalPerson> getAdditionalPerson(@Valid List<AdditionalPersonBean> additionalPersons) {
        return additionalPersons
                .stream()
                .map(additionalPersonBean -> {
                    AdditionalPerson person = new AdditionalPerson();
                    person.setFullName(additionalPersonBean.getFullName());
                    person.setEmail(additionalPersonBean.getEmail());
                    person.setPhone(additionalPersonBean.getPhone());

                    Identification identification = new Identification();
                    identification.setTypeIdentification(additionalPersonBean.getIdentification().getTypeIdentification());
                    identification.setNumberIdentification(additionalPersonBean.getIdentification().getNumberIdentification());
                    person.setIdentification(identification);
                    return person;

                }).collect(Collectors.toList());
    }

    private ActiveProduct getActiveProduct(ProductRequest prq) {
        ActiveProduct activeProduct = new ActiveProduct();
        activeProduct.setProductType(prq.getProductType());
        activeProduct.setHasCreditCard(prq.getActiveProduct().getHasCreditCard());

        if (activeProduct.isHasCreditCard()) {
            CreditCardBean creditCardBean = prq.getActiveProduct().getCreditCard();
            CreditCard creditCard = new CreditCard();
            creditCard.setNumber(creditCardBean.getNumber());
            creditCard.setExpirationDate(creditCardBean.getExpirationDate());
            creditCard.setAuditData(AuditDataUtil.create(null));
            activeProduct.setCreditCard(creditCard);
        }

        activeProduct.setCreditLimit(prq.getActiveProduct().getCreditLimit());
        activeProduct.setCreditLimitUsed(prq.getActiveProduct().getCreditLimitUsed());
        activeProduct.setCreditBalance(prq.getActiveProduct().getCreditBalance());
        return activeProduct;
    }

    private static PassiveProduct getPassiveProduct(ProductRequest prq) {
        PassiveProduct passiveProduct = new PassiveProduct();
        passiveProduct.setProductType(prq.getProductType());
        passiveProduct.setFreeCommission(prq.getPassiveProduct().getIsFreeCommission());
        passiveProduct.setAmountOfOpen(prq.getPassiveProduct().getAmountOfOpen());
        passiveProduct.setAccountNumber(prq.getPassiveProduct().getAccountNumber());

        Balance balance = new Balance();
        balance.setTotalAmount(prq.getPassiveProduct().getAmountOfOpen());
        passiveProduct.setBalance(balance);

        passiveProduct.setCommission(prq.getPassiveProduct().getInforToTransaction().getCommission());
        passiveProduct.setMaxMovementPerMonth(prq.getPassiveProduct().getInforToTransaction().getMaxPerMonth());
        return passiveProduct;
    }

    private Customer getCustomerOfCustomerBean(@Valid CustomerBean customerBean) {
        Customer customer = new Customer();
        customer.setCustomerType(customerBean.getCustomerType());
        customer.setCustomerId(customerBean.getCustomerId());
        return customer;
    }


}
