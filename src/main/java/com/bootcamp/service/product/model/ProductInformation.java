package com.bootcamp.service.product.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "products")
public class ProductInformation {

    @Id
    private String id;
    private String customerId;
    private String customerType;
    private List<ActiveProduct> activeProduct = new ArrayList<>();
    private List<PassiveProduct> passiveProduct = new ArrayList<>();
    private AuditData auditData;
}
