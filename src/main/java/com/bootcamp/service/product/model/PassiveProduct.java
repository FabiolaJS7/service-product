package com.bootcamp.service.product.model;

import com.bootcamp.service.product.constants.StatusProductConstants;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public abstract class PassiveProduct {

    private String id;
    private String accountNumber;
    private String status = StatusProductConstants.ENABLED;
    private List<PersonalInformationAdd> holders = new ArrayList<>();
    private List<PersonalInformationAdd> authorizedSignatories = new ArrayList<>();
    private AuditData auditData;

    private String productType;
    private double commissionMovement;
    private boolean isFreeCommission;
    private String maxMovementPerMonth;



    


}
