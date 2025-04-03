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
        activeProduct.setHasCreditCard(true);

        if (activeProduct.isHasCreditCard()) {
            CreditCard creditCard = new CreditCard();
            creditCard.setNumber("XXXX-XXXX-XXXXXX");
            creditCard.setExpirationDate(new Date());
            creditCard.setAuditData(AuditDataUtil.create("admin"));
            activeProduct.setCreditCard(creditCard);
        }

        activeProduct.setCreditLimit(prq.getActiveProduct().getCreditLimit());
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
