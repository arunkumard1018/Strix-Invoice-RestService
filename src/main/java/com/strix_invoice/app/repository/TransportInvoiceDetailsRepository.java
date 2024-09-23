package com.strix_invoice.app.repository;

import com.strix_invoice.app.Entity.invoice.TransportInvoiceDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TransportInvoiceDetailsRepository extends JpaRepository<TransportInvoiceDetails,Long> {
    List<TransportInvoiceDetails> findAllByInvoicesId(Long id);

    @Query("DELETE FROM TransportInvoiceDetails t WHERE t.invoices.id = :invoiceId ")
    void deleteByInvoiceId(@Param("invoiceId") Long invoiceId);
}
