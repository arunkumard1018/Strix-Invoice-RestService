package com.strix_invoice.app.projections.invoice;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.strix_invoice.app.Entity.Address;
import com.strix_invoice.app.Entity.invoice.TransportInvoiceDetails;
import com.strix_invoice.app.records.PaymentStatus;

import java.time.LocalDate;
import java.util.List;

public interface TransportInvoiceProjection {

    Long getId();
    LocalDate getInvoiceDate();
    String getInvoiceNo();
    PaymentStatus getPaymentStatus();
    Double getIgst();
    Double getCgst();
    Double getSgst();
    Double getDiscount();
    Double getInvoiceAmount();
    String getCustomerName();
    String getCustomerEmail();
    String getCustomerGST();

    Address getBillingAddress();
    Address getShippingAddress();

    List<TransportInvoiceDetails> getTransportInvoiceDetails();

    @JsonIgnore
    UsersInfoProjection getUsersInfo();
    interface UsersInfoProjection{
        Long getId();
    }
}
