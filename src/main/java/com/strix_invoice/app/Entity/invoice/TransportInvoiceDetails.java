/**
 * The {@code TransportInvoiceDetails$} class represents Functionalities
 *
 * @author ArunKumar D
 */

package com.strix_invoice.app.Entity.invoice;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.strix_invoice.app.records.GSTType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
@Entity
public class TransportInvoiceDetails{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "transport_invoice_seq_gen")
    @SequenceGenerator(name = "transport_invoice_seq_gen",
            sequenceName = "transport_invoice_seq", allocationSize = 50)
    private Long id;

    @Enumerated(EnumType.STRING)
    private GSTType gstType;

    private Double gstValue;

    private Double discount;

    private Double price;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "invoice_id", referencedColumnName = "id")
    private Invoices invoices;

    @NotNull
    private LocalDate date;

    @NotNull
    private String vehicleNo;

    @NotNull
    private String source;

    @NotNull
    private String destination;

}
