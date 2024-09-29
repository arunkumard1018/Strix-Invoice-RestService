package com.strix_invoice.app.projections.business;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.strix_invoice.app.projections.address.AddressDataProjection;
import com.strix_invoice.app.records.BusinessType;

public interface BusinessDataWithAddressProjection {
    Long getId();
    String getName();
    String getGstin();
    Integer getHsn();
    Integer getStateCode();
    String getBusinessLogo();
    BusinessType getBusinessType();

    AddressDataProjection getAddress();

    @JsonIgnore
    UsersInfoProjection getUsersInfo();

    interface UsersInfoProjection{
        Long getId();
    }

}
