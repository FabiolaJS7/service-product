package com.bootcamp.service.product.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public abstract class PassiveProduct {

    private String id;
    private String accountNumber;
    private String status;
    private List<PersonalInformationAdd> holders;
    private List<PersonalInformationAdd> authorizedSignatories;
    private AuditData auditData;

    public abstract double commissionMovement();
    public abstract boolean isFreeCommission();
    public abstract String maxMovementPerMonth();
    public abstract String productType();

}
