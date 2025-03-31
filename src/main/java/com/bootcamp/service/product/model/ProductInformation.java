package com.bootcamp.service.product.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collation = "products")
public class ProductInformation {

    @Id
    private String id;
    private String customerId;
    private List<ActiveProduct> activeProduct;
    private List<PassiveProduct> passiveProduct;
    private AuditData auditData;
}
