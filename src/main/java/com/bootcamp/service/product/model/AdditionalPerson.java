package com.bootcamp.service.product.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AdditionalPerson {

    private String fullName;
    private String email;
    private String phone;
    private Identification identification;
    private AuditData auditData;

}
