package com.strix_invoice.app.projections.invoice;

import com.strix_invoice.app.Entity.Business;
import com.strix_invoice.app.records.BusinessType;

import java.time.LocalDateTime;

public interface InvoiceType {

    Long getId();
    LocalDateTime getCreatedAt();
    BusinessTypeProjection getBusiness();

    interface BusinessTypeProjection{
        Long getId();
        String getName();
        BusinessType getBusinessType();
    }

}
