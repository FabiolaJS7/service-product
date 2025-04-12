package com.bootcamp.service.product.transfer;

import com.bootcamp.service.product.constants.ProductTypeConstants;
import com.bootcamp.service.product.model.*;
import com.bootcamp.service.product.util.AuditDataUtil;
import com.bootcamp.service.product.util.NumberRandomUtil;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProductTransfer {

    private static final Double DEFAULT_LINE = 3000.00;
    private static final Double ZERO = 0.00;

    public Product getProductOfProductRequest(ProductRequest prq) {
        Product product = new Product();
        product.setCustomer(getCustomerOfCustomerBean(prq.getCustomer()));
        product.setProductType(prq.getProductType());
        product.setAmountOfOpen(prq.getAmountOfOpen());
        product.setHasPlasticCard(ProductTypeConstants.SAVING_ACCOUNT.equals(prq.getProductType())
                || ProductTypeConstants.CREDIT_CARD.equals(prq.getProductType()));
        product.setAccountNumber(NumberRandomUtil.generateAccountNumber(prq.getProductType()));
        product.setBalance(buildBalance(product));
        product.setAuditData(AuditDataUtil.create(prq.getUserBank()));
        return product;
    }

    private Balance buildBalance(Product product) {
        Balance balance = new Balance();

        if (product.getProductType().equalsIgnoreCase(ProductTypeConstants.CREDIT_CARD)) {
            balance.setCreditLimit(DEFAULT_LINE); // se estable 3000 de linea de credito para nueva credit card (CC)
            balance.setCreditLimitUsed(ZERO);
            balance.setCreditEnabledToUse(ZERO);
        } else if (ProductTypeConstants.CREDIT_ACCOUNT.contains(product.getProductType())) {
            balance.setCreditLimit(DEFAULT_LINE); //cuenta de credito se estable 3000 de credito ya usados
            balance.setCreditLimitUsed(DEFAULT_LINE);
            balance.setCreditEnabledToUse(ZERO);
        } else {
            balance.setCreditLimit(ZERO);
            balance.setCreditLimitUsed(ZERO);
            balance.setCreditEnabledToUse(ZERO);
        }

        if (ProductTypeConstants.PASSIVE_PRODUCTS.contains(product.getProductType())) {
            balance.setTotalAmountInAccount(product.getAmountOfOpen());
        }
        return balance;
    }

    public ProductResponse getProductResponseOfProduct(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null");
        }
        ProductResponse productResponse = new ProductResponse();
        productResponse.setId(product.getId());
        productResponse.setProductType(product.getProductType());
        productResponse.setAmountOfOpen(product.getAmountOfOpen());
        productResponse.setAccountNumber(product.getAccountNumber());
        productResponse.setHasPlasticCard(product.getHasPlasticCard());

        CustomerBean customerBean = new CustomerBean();
        customerBean.setCustomerId(product.getCustomer().getCustomerId());
        productResponse.setCustomer(customerBean);

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

        productResponse.setCreatedDate(product.getAuditData().getCreatedAt());
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

    private Customer getCustomerOfCustomerBean(@Valid CustomerBean customerBean) {
        Customer customer = new Customer();
        customer.setCustomerType(customerBean.getCustomerType());
        customer.setCustomerId(customerBean.getCustomerId());
        return customer;
    }


}
