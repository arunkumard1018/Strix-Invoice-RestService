/**
 * The {@code TransportInvoiceDetailsService$} class represents Functionalities
 *
 * @author ArunKumar D
 */

package com.strix_invoice.app.service.invoice;

import com.strix_invoice.app.Entity.Business;
import com.strix_invoice.app.Entity.invoice.Invoices;
import com.strix_invoice.app.Entity.invoice.TransportInvoiceDetails;
import com.strix_invoice.app.exceptions.custom.InvalidRequestBodyException;
import com.strix_invoice.app.model.invoice.InvoiceModel;
import com.strix_invoice.app.model.invoice.TransportInvoiceModel;
import com.strix_invoice.app.repository.BusinessRepository;
import com.strix_invoice.app.repository.TransportInvoiceDetailsRepository;
import com.strix_invoice.app.service.BusinessService;
import com.strix_invoice.app.utility.generators.InvoiceDetailsGenerator;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
public class TransportInvoiceDetailsService {

    @Autowired
    private TransportInvoiceDetailsRepository transportRepository;
    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void createTransportInvoice(Invoices invoice, List<TransportInvoiceModel> transportModel, Business business) {

        log.info("Generating  Transport Invoices for Business with ID {}", business.getId());

        if (transportModel != null) {
            List<TransportInvoiceDetails> transportInvoiceDetailsList = new ArrayList<>();

            for (TransportInvoiceModel model : transportModel) {
                TransportInvoiceDetails invoiceDetails = InvoiceDetailsGenerator
                        .generateTransportInvoiceDetails(model);

                invoiceDetails.setInvoices(invoice);
                transportInvoiceDetailsList.add(invoiceDetails);
            }

            transportRepository.saveAll(transportInvoiceDetailsList);
            log.info("Invoices Generated Successfully with invoices ID {} for Transport Business with ID {}", invoice.getId(), business.getId());

        } else {
            throw new InvalidRequestBodyException("Transport Billing Details Not Provided");
        }
    }

    @Transactional
    public void updateTransportInvoice(List<TransportInvoiceModel> transportInvoiceModel, Invoices invoice) {

        List<TransportInvoiceDetails> transportInvoiceDetails =  transportRepository.findAllByInvoicesId(invoice.getId());

        Map<Long, TransportInvoiceDetails> existingTransportDetailsMap = transportInvoiceDetails
                .stream()
                .collect(Collectors.toMap(TransportInvoiceDetails::getId, Function.identity()));

        List<TransportInvoiceDetails> newDetails = new ArrayList<>();
        List<TransportInvoiceDetails> updatedDetails = new ArrayList<>();

        for (TransportInvoiceModel model : transportInvoiceModel){
            TransportInvoiceDetails invoiceDetails;

            if (model.getId() != null && existingTransportDetailsMap.containsKey(model.getId())){
                // Update existing details
                invoiceDetails = existingTransportDetailsMap.get(model.getId());
                if (!invoiceDetails.equals(model)) {
                    System.out.println("Updating + " + model.getVehicleNo()+" ID "+model.getId());
                    InvoiceDetailsGenerator.updateTransportInvoiceDetails(invoiceDetails, model);
                    updatedDetails.add(invoiceDetails);
                }
                existingTransportDetailsMap.remove(model.getId());

            }else {
                // Create new details
                invoiceDetails = InvoiceDetailsGenerator.generateTransportInvoiceDetails(model);
                invoiceDetails.setInvoices(invoice);
                /* Adding the New Transport Details */
                newDetails.add(invoiceDetails);
            }
        }

        transportRepository.deleteAllInBatch(existingTransportDetailsMap.values());
        transportRepository.saveAll(updatedDetails);
        transportRepository.saveAll(newDetails);
    }

    @Transactional
    public void deleteAllDetails(Long invoiceId) {
        transportRepository.deleteAll();
    }
}
