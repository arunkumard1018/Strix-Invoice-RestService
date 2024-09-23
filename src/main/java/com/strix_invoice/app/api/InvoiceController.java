/**
 * The {@code InvoiceController$} class represents Functionalities
 *
 * @author ArunKumar D
 */

package com.strix_invoice.app.api;

import com.strix_invoice.app.Entity.invoice.Invoices;
import com.strix_invoice.app.Entity.invoice.RetailInvoiceDetails;
import com.strix_invoice.app.model.UsersPrincipal;
import com.strix_invoice.app.model.invoice.InvoiceModel;
import com.strix_invoice.app.projections.invoice.InvoiceProjection;
import com.strix_invoice.app.projections.invoice.InvoiceType;
import com.strix_invoice.app.projections.invoice.TransportInvoiceProjection;
import com.strix_invoice.app.records.BusinessType;
import com.strix_invoice.app.service.BusinessService;
import com.strix_invoice.app.service.invoice.InvoiceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("${api.base-path}")
public class InvoiceController {
    @Autowired
    private InvoiceService invoiceService;
    @Autowired
    private BusinessService businessService;


    @PostMapping("/business/{businessId}/invoices")
    public ResponseEntity createInvoice(@PathVariable Long businessId,
                                        @RequestBody InvoiceModel invoiceModel,
                                        @AuthenticationPrincipal UsersPrincipal principal) {
        Long userId = principal.getUserId();
        log.info("User ID {} is attempting to create an invoices for Business ID {}.", userId, businessId);
        log.debug("Invoices details: {}", invoiceModel);

        Invoices invoices = invoiceService.createInvoice(invoiceModel, businessId, userId);

        log.info("Invoices creation request processed successfully with invoices ID {} for User ID {} and Business ID {}.", invoices.getId(), userId, businessId);
        return ResponseEntity.status(HttpStatus.CREATED).body(Collections.singletonMap("message", "success"));
    }

    @PutMapping("/invoices/{invoiceId}")
    public ResponseEntity updateInvoice(@RequestBody InvoiceModel invoiceModel,
                                        @PathVariable Long invoiceId,
                                        @AuthenticationPrincipal UsersPrincipal principal) {

        Long userId = principal.getUserId();
        log.info("User ID {} is attempting to Update an invoices for ID {}.", userId, invoiceId);

        invoiceService.updateInvoice(invoiceModel, invoiceId, userId);

        log.info("Invoices Update request processed successfully with invoices ID {} for User ID {}.", invoiceId, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body("SUCCESS");
    }

    @GetMapping("/business/{businessId}/invoices")
    public ResponseEntity<List<InvoiceProjection>> retrieveAllInvoicesForBusiness(@PathVariable Long businessId,
                                                                                  @AuthenticationPrincipal UsersPrincipal principal) {
        Long userId = principal.getUserId();
        log.info("Request for Resource Invoices with Business ID {} by user with ID {}", businessId, userId);

        List<InvoiceProjection> invoiceProjections = invoiceService.retrieveAllInvoicesForBusiness(businessId, userId);

        log.info("Successfully Dispatched Request for Resource Invoices for Business ID {} for user with ID {}", businessId, userId);
        return ResponseEntity.status(HttpStatus.OK).body(invoiceProjections);
    }

    @GetMapping("/invoices/{invoiceId}")
    public ResponseEntity retrieveInvoices(@PathVariable Long invoiceId,
                                           @AuthenticationPrincipal UsersPrincipal principal) {
        Long userId = principal.getUserId();
        log.info("Request for Resource Invoices with ID {} by user with ID {}", invoiceId, userId);

//        InvoiceType invoiceType = invoiceService.getInvoiceType(invoiceId);

//        if(invoiceType.getBusiness().getBusinessType().equals(BusinessType.TRANSPORT)){

        TransportInvoiceProjection invoices = invoiceService.getTransportInvoice(invoiceId, userId);
        log.info("Successfully Dispatched Request for Resource Invoices with ID {} by user with ID {}", invoiceId, userId);
        return ResponseEntity.status(HttpStatus.OK).body(invoices);

//        }else {
//            RetailInvoiceDetails invoices = invoiceService.getRetailInvoice(invoiceId,userId);
//            log.info("Successfully Dispatched Request for Resource Invoices with ID {} by user with ID {}", invoiceId, userId);
//            return ResponseEntity.status(HttpStatus.OK).body(invoices);
//        }
    }

    @DeleteMapping("/invoices/{invoiceId}")
    public ResponseEntity deleteInvoice(@PathVariable Long invoiceId,
                                        @AuthenticationPrincipal UsersPrincipal principal){

        Long userId = principal.getUserId();
        log.info("Request to Delete Invoice with ID {}  By Users with ID {} ",invoiceId,userId);

        invoiceService.deleteInvoice(invoiceId,userId);

        log.info("Request to delete invoice with ID {} by User with ID {} processed successfully.",invoiceId,userId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(Collections.singletonMap("message","success"));

    }
}
