package com.strix_invoice.app.projections.business;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.strix_invoice.app.projections.address.AddressDataProjection;

public interface BusinessDataWithAddressProjection {
    Long getId();
    String getName();
    String getGstin();
    Integer getHsn();
    Integer getStateCode();
    String getBusinessLogo();


    AddressDataProjection getAddress();

    @JsonIgnore
    UsersInfoProjection getUsersInfo();

    interface UsersInfoProjection{
        Long getId();
    }

}
