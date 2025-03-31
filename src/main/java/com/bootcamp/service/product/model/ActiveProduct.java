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
public class ActiveProduct {

    private String id;
    private String accountNumber;
    private boolean hasCreditCard;
    private Double creditLimit;
    private Double creditLimitEnabled;
    private Double creditLimitUsed;
    private String productType;
    private String status;
    private List<PersonalInformationAdd> holders;
    private List<PersonalInformationAdd> authorizedSignatories;
    private AuditData auditData;


}
