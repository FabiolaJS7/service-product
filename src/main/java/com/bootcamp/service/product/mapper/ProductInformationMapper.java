package com.bootcamp.service.product.mapper;

import com.bootcamp.service.product.model.*;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ProductInformationMapper {

    public ProductResponse getProductResponseOfProductInformation(ProductInformation productInformation) {
        ProductResponse productResponse = new ProductResponse();
        productResponse.setId(productInformation.getId());
        productResponse.setCustomerId(productInformation.getCustomerId());

        List<ProductBean> productBeanList = new ArrayList<>();
        productInformation.getActiveProduct().forEach(activeProduct -> {
            ProductBean productBean = new ProductBean();
            productBean.setProductType(activeProduct.getProductType());
            productBean.setStatus(activeProduct.getStatus());
            productBean.setFamily("ACTIVE PRODUCT");
            productBean.setHolders(getPersonalInformationAddBean(activeProduct.getHolders()));
            productBean.setAuthorizedSignatories(getPersonalInformationAddBean(activeProduct.getAuthorizedSignatories()) );
            productBeanList.add(productBean);
        });

        productInformation.getPassiveProduct().forEach(passiveProduct -> {
            ProductBean productBean = new ProductBean();
            productBean.setProductType(passiveProduct.getProductType());
            productBean.setStatus(passiveProduct.getStatus());
            productBean.setFamily("PASSIVE PRODUCT");
            productBean.setHolders(getPersonalInformationAddBean(passiveProduct.getHolders()));
            productBean.setAuthorizedSignatories(getPersonalInformationAddBean(passiveProduct.getAuthorizedSignatories()) );
            productBeanList.add(productBean);
        });

        productResponse.setProducts(productBeanList);
        return productResponse;
    }

    private List<PersonalInformationAddBean> getPersonalInformationAddBean(List<AdditionalPerson> holders) {
        List<PersonalInformationAddBean> addAsHolder = new ArrayList<>();
        holders.forEach(holder -> {
            PersonalInformationAddBean personalInformationAddBean = new PersonalInformationAddBean();
            personalInformationAddBean.setName(holder.getFullName());
            personalInformationAddBean.setIdentification(holder.getIdentification());
            personalInformationAddBean.setTypeIdentification(holder.getTypeIdentification());
            personalInformationAddBean.setEmail(holder.getEmail());
            personalInformationAddBean.setPhone(holder.getPhone());
            addAsHolder.add(personalInformationAddBean);
        });

        return addAsHolder;

    }
}
