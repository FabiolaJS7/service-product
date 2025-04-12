package com.bootcamp.service.product.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuditData {

    private LocalDate createdAt;
    private LocalDate updatedAt;
    private String createdBy;
    private String updatedBy;
}
