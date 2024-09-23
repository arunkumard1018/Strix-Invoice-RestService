/**
 * The {@code Invoices$} class represents Functionalities
 *
 * @author ArunKumar D
 */

package com.strix_invoice.app.Entity.invoice;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.strix_invoice.app.Entity.Address;
import com.strix_invoice.app.Entity.Business;
import com.strix_invoice.app.Entity.UsersInfo;
import com.strix_invoice.app.records.PaymentStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Entity
@Setter
@Getter
public class Invoices {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate invoiceDate;

    @Column(unique = true, nullable = false)
    private String invoiceNo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus paymentStatus;

    @Column(nullable = true)
    private Double igst;

    @Column(nullable = true)
    private Double cgst;

    @Column(nullable = true)
    private Double sgst;

    private Double discount;

    private Double invoiceAmount;


    private Long customerId;
    private String customerName;
    private String customerEmail;
    private String customerGST;

    @OneToMany(mappedBy = "invoices", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<TransportInvoiceDetails> transportInvoiceDetails = new ArrayList<>();

    @JsonManagedReference
    @OneToMany(mappedBy = "invoices", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RetailInvoiceDetails> retailInvoiceDetails = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "business_id", referencedColumnName = "id")
    private Business business;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "users_info_id", referencedColumnName = "id")
    private UsersInfo usersInfo;

    @OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "shipping_address", referencedColumnName = "id")
    private Address shippingAddress;

    @OneToOne(optional = false, fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "billing_address", referencedColumnName = "id")
    private Address billingAddress;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

}
