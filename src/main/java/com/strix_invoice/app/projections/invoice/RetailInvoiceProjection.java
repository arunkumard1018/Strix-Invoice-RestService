package com.strix_invoice.app.projections.invoice;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.strix_invoice.app.Entity.Address;
import com.strix_invoice.app.Entity.invoice.RetailInvoiceDetails;
import com.strix_invoice.app.records.PaymentStatus;
import java.time.LocalDate;
import java.util.List;

public interface RetailInvoiceProjection {

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

    List<RetailInvoiceDetails> getRetailInvoiceDetails();

    @JsonIgnore
    UsersInfoProjection getUsersInfo();
    interface UsersInfoProjection{
        Long getId();
    }
}
