package com.bootcamp.service.product.util;

import com.bootcamp.service.product.model.AuditData;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class AuditDataUtil {

    public static AuditData create(String createdBy) {
        AuditData auditData = new AuditData();
        auditData.setCreatedAt(new Date());
        auditData.setUpdatedAt(new Date());
        auditData.setCreatedBy(createdBy);
        return auditData;
    }

    public static void update(AuditData auditData, String createdBy) {
        auditData.setUpdatedAt(new Date());
        auditData.setUpdatedBy(createdBy);
    }
}
