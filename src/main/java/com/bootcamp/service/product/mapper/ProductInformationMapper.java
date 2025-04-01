package com.bootcamp.service.product.mapper;

import com.bootcamp.service.product.model.ProductBean;
import com.bootcamp.service.product.model.ProductInformation;
import com.bootcamp.service.product.model.ProductResponse;
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
            productBean.setAccountNumber(activeProduct.getAccountNumber());
            productBean.setProductType(activeProduct.getProductType());
            productBean.setStatus(activeProduct.getStatus());
            productBean.setFamily("ACTIVE PRODUCT");
            productBeanList.add(productBean);
        });

        productInformation.getPassiveProduct().forEach(passiveProduct -> {
            ProductBean productBean = new ProductBean();
            productBean.setAccountNumber(passiveProduct.getAccountNumber());
            productBean.setProductType(passiveProduct.getProductType());
            productBean.setStatus(passiveProduct.getStatus());
            productBean.setFamily("PASSIVE PRODUCT");
            productBeanList.add(productBean);
        });

        productResponse.setProducts(productBeanList);
        return productResponse;
    }
}
