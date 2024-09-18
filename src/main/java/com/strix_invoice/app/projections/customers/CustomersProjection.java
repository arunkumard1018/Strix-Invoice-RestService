package com.strix_invoice.app.projections.customers;


public interface CustomersProjection {

    Long getId();
    String getName();
    Long getPhone();
    String getEmail();
    String getPan();
    String getGstin();

}
