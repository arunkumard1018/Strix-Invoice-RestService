package com.strix_invoice.app.projections.business;

import com.fasterxml.jackson.annotation.JsonIgnore;

public interface BusinessProjection {
    Long getId();
    String getName();
    String getGstin();
    Integer getHsn();
    Integer getStateCode();
    String getBusinessLogo();



}
