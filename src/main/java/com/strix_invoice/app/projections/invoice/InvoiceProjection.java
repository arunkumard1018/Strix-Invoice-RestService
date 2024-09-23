package com.strix_invoice.app.projections.invoice;

import com.strix_invoice.app.records.PaymentStatus;

import java.time.LocalDate;

public interface InvoiceProjection {
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

    UsersInfoProjection getUsersInfo();

    interface UsersInfoProjection{
        Long getId();
    }
}
