package com.bootcamp.service.product.util;

import com.bootcamp.service.product.model.AuditData;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class AuditDataUtil {

    public static AuditData create(String createdBy) {
        AuditData auditData = new AuditData();
        auditData.setCreatedAt(LocalDate.now());
        auditData.setUpdatedAt(LocalDate.now());
        auditData.setCreatedBy(createdBy);
        return auditData;
    }

    public static void update(AuditData auditData, String createdBy) {
        auditData.setUpdatedAt(LocalDate.now());
        auditData.setUpdatedBy(createdBy);
    }
}
