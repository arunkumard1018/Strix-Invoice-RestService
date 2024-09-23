/**
 * The {@code RetailInvoiceDetailsService$} class represents Functionalities
 *
 * @author ArunKumar D
 */

package com.strix_invoice.app.service.invoice;

import com.strix_invoice.app.Entity.Business;
import com.strix_invoice.app.Entity.invoice.Invoices;
import com.strix_invoice.app.Entity.invoice.RetailInvoiceDetails;
import com.strix_invoice.app.model.invoice.InvoiceModel;
import com.strix_invoice.app.model.invoice.RetailInvoiceModel;
import com.strix_invoice.app.repository.RetailInvoiceDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RetailInvoiceDetailsService {

    @Autowired
    private RetailInvoiceDetailsRepository retailInvoiceDetailsRepository;

    @Transactional
    public Invoices createRetailInvoice(Invoices invoice, List<RetailInvoiceModel> invoiceModel, Business business) {
        return null;
    }

    @Transactional
    public void updateRetailInvoice(InvoiceModel invoiceModel,Invoices invoice){

    }

    @Transactional
    public void deleteAll(Long retailInvoiceDetails) {
//        retailInvoiceDetailsRepository.deleteAllInBatch(retailInvoiceDetails);
        return;
    }
}
