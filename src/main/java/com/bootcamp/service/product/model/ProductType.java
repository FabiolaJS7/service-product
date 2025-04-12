package com.bootcamp.service.product.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@AllArgsConstructor
@Document(collection = "product_type")
public class ProductType {

    @Id
    private String id;
    private String code;
    private String description;
    private String familyType;
    private Boolean isFreeMaintenanceCommission;
    private Boolean isFreeNumberMovement;
    private Boolean isFreeCommissionPerMovement;
    private int maxMovementPerMonth;
    private String dateToUniqueMovement;
    private Double maintenanceCommission;
    private Double movementCommission;

}
