package com.strix_invoice.app.repository;

import com.strix_invoice.app.Entity.invoice.RetailInvoiceDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RetailInvoiceDetailsRepository extends JpaRepository<RetailInvoiceDetails,Long> {
}
