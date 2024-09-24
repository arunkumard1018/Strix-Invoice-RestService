package com.strix_invoice.app.projections.usersInfo;


import com.strix_invoice.app.Entity.Business;
import com.strix_invoice.app.records.BusinessType;

import java.util.List;

public interface UsersInfoProjection {

    Long getId();
    String getName();
    BusinessProjection getActiveBusiness();
    List<BusinessProjection> getBusinesses();

    interface BusinessProjection{
        Long getId();
        String getName();
        String getBusinessLogo();
        BusinessType getBusinessType();
        String getInvoicePrefix();
        Integer getInvoiceSeq();
    }
}
