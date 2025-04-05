package com.bootcamp.service.product.model;

import org.springframework.data.annotation.Id;
import java.util.List;


public class ProductInformation {

    @Id
    private String id;
    private String customerId;
    private String customerType;
    private List<ActiveProduct> activeProduct;
    private List<PassiveProduct> passiveProduct;
    private AuditData auditData;
}
