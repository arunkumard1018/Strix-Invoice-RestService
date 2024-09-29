package com.strix_invoice.app.projections.business;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.strix_invoice.app.records.BusinessType;

public interface BusinessInfoProjection {

    Long getId();
    String getName();
    BusinessType getBusinessType();

    @JsonIgnore
    UsersInfoProjection getUsersInfo();

    interface UsersInfoProjection{
        Long getId();
    }
}

