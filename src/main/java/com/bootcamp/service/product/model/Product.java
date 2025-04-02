package com.bootcamp.service.product.model;

import com.bootcamp.service.product.constants.StatusProductConstants;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Product {

    @Id
    private String id;
    private String productType;
    private String status = StatusProductConstants.ENABLED;
    private Customer customer;
    private List<AdditionalPerson> holders = new ArrayList<>();
    private List<AdditionalPerson> authorizedSignatories = new ArrayList<>();
    private AuditData auditData;
}
