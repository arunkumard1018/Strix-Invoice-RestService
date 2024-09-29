package com.strix_invoice.app.projections.business;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.strix_invoice.app.records.BusinessType;

public interface BusinessProjection {
    Long getId();
    String getName();
    String getGstin();
    Integer getHsn();
    Integer getStateCode();
    String getBusinessLogo();
    BusinessType getBusinessType();
    String getInvoicePrefix();
    Integer getInvoiceSeq();

}
