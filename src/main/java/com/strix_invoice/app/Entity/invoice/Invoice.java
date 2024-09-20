/**
 * The {@code Invoice$} class represents Functionalities
 *
 * @author ArunKumar D
 */

package com.strix_invoice.app.Entity.invoice;

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
public class Invoice {
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
    private Double sgst;

    @Column(nullable = true)
    private Double cgst;

    @Column(nullable = true)
    private Double igst;

    private Double discount;

    private Double invoiceAmount;

    @OneToMany(mappedBy = "invoice", fetch = FetchType.LAZY)
    private List<TransportInvoiceDetails> transportInvoiceDetails = new ArrayList<>();

    @OneToMany(mappedBy = "invoice", fetch = FetchType.LAZY)
    private List<RetailInvoiceDetails> retailInvoiceDetails = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "business_id", referencedColumnName = "id")
    private Business business;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "users_info_id", referencedColumnName = "id")
    private UsersInfo usersInfo;

    private Long customerId;
    @NotNull
    private String customerName;
    private String customerEmail;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shipping_address", referencedColumnName = "id")
    private Address shippingAddress;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "billing_address", referencedColumnName = "id")
    private Address billingAddress;

    private String customerGST;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

}
