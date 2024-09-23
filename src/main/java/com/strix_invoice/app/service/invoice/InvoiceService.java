/**
 * The {@code InvoiceService$} class represents Functionalities
 *
 * @author ArunKumar D
 */

package com.strix_invoice.app.service.invoice;

import com.strix_invoice.app.Entity.Business;
import com.strix_invoice.app.Entity.invoice.Invoices;
import com.strix_invoice.app.Entity.invoice.RetailInvoiceDetails;
import com.strix_invoice.app.exceptions.custom.BusinessNotFoundException;
import com.strix_invoice.app.exceptions.custom.ResourceNotFoundException;
import com.strix_invoice.app.model.invoice.InvoiceModel;
import com.strix_invoice.app.projections.business.BusinessInfoProjection;
import com.strix_invoice.app.projections.invoice.InvoiceProjection;
import com.strix_invoice.app.projections.invoice.InvoiceType;
import com.strix_invoice.app.projections.invoice.TransportInvoiceProjection;
import com.strix_invoice.app.records.BusinessType;
import com.strix_invoice.app.repository.BusinessRepository;
import com.strix_invoice.app.repository.InvoiceRepository;
import com.strix_invoice.app.service.BusinessService;
import com.strix_invoice.app.utility.Utility;
import com.strix_invoice.app.utility.generators.InvoiceGenerator;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
public class InvoiceService {

    @Autowired
    private InvoiceRepository invoiceRepository;
    @Autowired
    private BusinessService businessService;
    @Autowired
    private TransportInvoiceDetailsService transportInvoiceDetailsService;
    @Autowired
    private RetailInvoiceDetailsService retailInvoiceDetailsService;
    @Autowired
    private BusinessRepository businessRepository;
    @PersistenceContext
    private EntityManager entityManager;


    @Transactional
    public Invoices createInvoice(InvoiceModel invoiceModel, Long businessId, Long userId) {

        Business business = businessService.getBusiness(businessId).
                orElseThrow(() -> new BusinessNotFoundException("Business with ID " + businessId + " not found."));

        if (!business.getUsersInfo().getId().equals(userId)) {
            throw new AccessDeniedException("User with ID " + userId + "Does not have permission to create invoice for this business");
        }

        Invoices invoice = InvoiceGenerator.generateInvoice(invoiceModel);
        invoice.setBusiness(business);
        invoice.setUsersInfo(business.getUsersInfo());

        /* Setting Invoice No */
        invoice.setInvoiceNo(invoiceModel.getInvoiceNo());
        business.setInvoiceSeq(business.getInvoiceSeq() + 1);
        businessRepository.save(business);

        Invoices savedInvoice = invoiceRepository.save(invoice);

        if (business.getBusinessType().equals(BusinessType.TRANSPORT)) {
            transportInvoiceDetailsService.createTransportInvoice(savedInvoice, invoiceModel.getTransportInvoiceModels(), business);

        } else {
            retailInvoiceDetailsService.createRetailInvoice(savedInvoice, invoiceModel.getRetailInvoiceModels(), business);
        }
        return savedInvoice;
    }

    @Transactional
    public void updateInvoice(InvoiceModel invoiceModel, Long invoiceId, Long userId) {

        Invoices invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice with ID " + invoiceId + " Not Found"));

        if (!invoice.getUsersInfo().getId().equals(userId)) {
            throw new AccessDeniedException("user with ID " + userId + " does not permission to Update this Resource Invoice with ID " + invoiceId);
        }

        InvoiceGenerator.updateInvoice(invoice, invoiceModel);
//        Invoices updatedInvoice = invoiceRepository.save(invoice);

        if (invoice.getBusiness().getBusinessType().equals(BusinessType.TRANSPORT)) {
            System.out.println("UPDATING CUSTOMER INVOICE");
            transportInvoiceDetailsService.updateTransportInvoice(invoiceModel.getTransportInvoiceModels(), invoice);

        } else {
            retailInvoiceDetailsService.updateRetailInvoice(invoiceModel, invoice);
        }
        invoiceRepository.save(invoice);

    }

    public TransportInvoiceProjection getTransportInvoice(Long invoiceId, Long userId) {

        TransportInvoiceProjection invoice = invoiceRepository.findByTransportInvoiceProjection(invoiceId)
                .orElseThrow(() -> new ResourceNotFoundException("Request Resource with ID " + invoiceId + " Not Found"));

        if (!invoice.getUsersInfo().getId().equals(userId)) {
            throw new AccessDeniedException("Does not Have permission to access this resource invoices with ID " + invoiceId);
        }

        return invoice;
    }

    public RetailInvoiceDetails getRetailInvoice(Long invoiceId, Long userId) {
        return null;
    }

    public List<InvoiceProjection> retrieveAllInvoicesForBusiness(Long businessId, Long userId) {
        BusinessInfoProjection businessInfo = businessService.findBusinessInfo(businessId)
                .orElseThrow(() -> new BusinessNotFoundException("Business With Id " + businessId + " not found."));

        if (!businessInfo.getUsersInfo().getId().equals(userId)) {
            throw new AccessDeniedException("User With ID " + userId + "Does not Have permission to access the resource invoices with business with ID " + businessId);
        }

        return invoiceRepository.findAllInvoiceByProjection(businessId);
    }

    public InvoiceType getInvoiceType(Long invoiceId) {
        InvoiceType invoiceType = invoiceRepository.findByInvoiceTypeProjection(invoiceId)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice with ID " + invoiceId + " not found."));
        return invoiceType;
    }

    @Transactional
    public void deleteInvoice(Long invoiceId, Long userId) {
        Invoices invoices = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice With ID " + invoiceId + " not found."));

        if (!invoices.getUsersInfo().getId().equals(userId)) {
            throw new AccessDeniedException("User with ID " + userId + " does not have access to Delete this Resource Invoice With ID " + invoiceId);
        }
        invoiceRepository.deleteById(invoiceId);
    }
}
