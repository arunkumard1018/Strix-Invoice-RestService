package com.strix_invoice.app.projections.business;

public interface BusinessProjection {
    Long getId();
    String getName();
    String getGstin();
    Integer getHsn();
    Integer getStateCode();
    String getBusinessLogo();


}
