package com.bootcamp.service.product.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "plastic_card")
public class PlasticCard {

    @Id
    private String id;
    private String productId;
    private String number;
    private String type; //CREDIT, DEBIT
    private String expirationDate;
    private AuditData auditData;
}
