package com.strix_invoice.app.projections.customers;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.strix_invoice.app.projections.address.AddressDataProjection;

public interface CustomersWithAddressProjection {
    Long getId();
    String getName();
    Long getPhone();
    String getEmail();
    String getPan();
    String getGstin();
    AddressDataProjection getBillingAddress();
    AddressDataProjection getShippingAddress();

//    @JsonIgnore
    UsersInfoProjection getUsersInfo();
    interface UsersInfoProjection{
        Long getId();
    }
}
