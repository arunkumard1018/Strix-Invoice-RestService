/**
 * The {@code InvoiceModel$} class represents Functionalities
 *
 * @author ArunKumar D
 */

package com.strix_invoice.app.model.invoice;

import com.strix_invoice.app.Entity.Address;
import com.strix_invoice.app.model.AddressModel;
import com.strix_invoice.app.records.PaymentStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Setter
@Getter
public class InvoiceModel {

    /**
     * Invoices Details to Create Invoices
     * */
    private LocalDate invoiceDate;
    private String invoiceNo;
    private PaymentStatus paymentStatus;
    private Double igst;
    private Double cgst;
    private Double sgst;
    private Double discount;
    private Double invoiceAmount;

    /**
     * Customer Details
     * */
    private Long customerId;
    private String customerName;
    private String customerEmail;
    private AddressModel shippingAddress;
    private AddressModel billingAddress;
    private String customerGST;

    /**
     * Transport Invoices Details
     * */
    private List<TransportInvoiceModel> transportInvoiceModels;

    /***
     * Retail Business Details
     * */
    private List<RetailInvoiceModel> retailInvoiceModels;
}
