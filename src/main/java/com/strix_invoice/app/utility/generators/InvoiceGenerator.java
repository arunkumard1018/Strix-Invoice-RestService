/**
 * The {@code InvoiceGenerator$} class represents Functionalities
 *
 * @author ArunKumar D
 */

package com.strix_invoice.app.utility.generators;

import com.strix_invoice.app.Entity.Address;
import com.strix_invoice.app.Entity.invoice.Invoices;
import com.strix_invoice.app.model.AddressModel;
import com.strix_invoice.app.model.invoice.InvoiceModel;
import com.strix_invoice.app.utility.Mapper;
import com.strix_invoice.app.utility.Updater;

public class InvoiceGenerator {

    public static Invoices generateInvoice(InvoiceModel invoiceModel){
        Invoices invoices = new Invoices();

        /*  Setting Invoices  Details */
        invoices.setInvoiceDate(invoiceModel.getInvoiceDate());
        invoices.setInvoiceDate(invoiceModel.getInvoiceDate());
        invoices.setPaymentStatus(invoiceModel.getPaymentStatus());
        invoices.setIgst(invoiceModel.getIgst());
        invoices.setCgst(invoiceModel.getCgst());
        invoices.setSgst(invoiceModel.getSgst());
        invoices.setDiscount(invoiceModel.getDiscount());
        invoices.setInvoiceAmount(invoiceModel.getInvoiceAmount());


        /* Setting Customers Details */
        invoices.setCustomerName(invoiceModel.getCustomerName());
        invoices.setCustomerEmail(invoiceModel.getCustomerEmail());
        invoices.setCustomerGST(invoiceModel.getCustomerGST());
        invoices.setCustomerId(invoiceModel.getCustomerId());

        /*Setting Shipping Address*/
        if(invoiceModel.getShippingAddress() != null){
            AddressModel shippingAddressModel = invoiceModel.getShippingAddress();
            Address shippingAddress = Mapper.mapAddressModelToAddress(shippingAddressModel);
            invoices.setShippingAddress(shippingAddress);
        }

        /*Setting Billing Address*/
        Address billingAddress = Mapper.mapAddressModelToAddress(invoiceModel.getBillingAddress());
        invoices.setBillingAddress(billingAddress);

        return invoices;
    }


    public static void updateInvoice(Invoices invoice, InvoiceModel invoiceModel) {

        /* Updating Invoice Details */
        invoice.setInvoiceDate(invoiceModel.getInvoiceDate());
        invoice.setInvoiceDate(invoiceModel.getInvoiceDate());
        invoice.setPaymentStatus(invoiceModel.getPaymentStatus());
        invoice.setIgst(invoiceModel.getIgst());
        invoice.setCgst(invoiceModel.getCgst());
        invoice.setSgst(invoiceModel.getSgst());
        invoice.setDiscount(invoiceModel.getDiscount());
        invoice.setInvoiceAmount(invoiceModel.getInvoiceAmount());

        /* Updating Customer Details */
        invoice.setCustomerName(invoiceModel.getCustomerName());
        invoice.setCustomerEmail(invoiceModel.getCustomerEmail());
        invoice.setCustomerGST(invoiceModel.getCustomerGST());
        invoice.setCustomerId(invoiceModel.getCustomerId());

        /* Updating Shipping Address */
        if(invoiceModel.getShippingAddress() != null){
            Updater.updateAddress(invoice.getShippingAddress(),invoiceModel.getShippingAddress());
        }
        /*Update Billing Address*/
       Updater.updateAddress(invoice.getBillingAddress(),invoiceModel.getBillingAddress());

    }

}
