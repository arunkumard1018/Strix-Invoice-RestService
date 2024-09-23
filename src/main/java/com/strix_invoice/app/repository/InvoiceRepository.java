package com.strix_invoice.app.repository;

import com.strix_invoice.app.Entity.invoice.Invoices;
import com.strix_invoice.app.projections.invoice.InvoiceProjection;
import com.strix_invoice.app.projections.invoice.InvoiceType;
import com.strix_invoice.app.projections.invoice.TransportInvoiceProjection;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface InvoiceRepository extends JpaRepository<Invoices, Long> {

    @Query("SELECT i FROM Invoices i WHERE i.business.id = :businessId")
    List<InvoiceProjection> findAllInvoiceByProjection(@Param("businessId") Long businessId);

    @Query("SELECT i FROM Invoices i WHERE i.id = :invoiceId")
    Optional<InvoiceType> findByInvoiceTypeProjection(@Param("invoiceId") Long invoiceId);

    @EntityGraph(attributePaths = {"shippingAddress", "billingAddress", "transportInvoiceDetails"})
    @Query("SELECT i FROM Invoices i WHERE i.id = :invoiceId")
    Optional<TransportInvoiceProjection> findByTransportInvoiceProjection(@Param("invoiceId") Long invoiceId);

    @EntityGraph(attributePaths = {"shippingAddress", "billingAddress"})
    Optional<Invoices> findById(Long id);

    @Query("DELETE FROM Invoices i WHERE i.id = :invoiceId")
    void deleteInvoicesByID(@Param("invoiceId") Long invoiceId);
}
