package com.strix_invoice.app.projections.address;

import com.fasterxml.jackson.annotation.JsonIgnore;

public interface AddressDataProjection {
    @JsonIgnore
    Long getId();

    String getCity();
    String getAddress();
    String getState();
    Integer getZip();
}


