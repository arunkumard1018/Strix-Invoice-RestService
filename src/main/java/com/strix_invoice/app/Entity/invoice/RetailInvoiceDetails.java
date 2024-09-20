/**
 * The {@code RetailInvoiceDetails$} class represents Functionalities
 *
 * @author ArunKumar D
 */

package com.strix_invoice.app.Entity.invoice;

import com.strix_invoice.app.records.GSTType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class RetailInvoiceDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private GSTType gstType;

    private Double gstValue;

    private Double discount;

    private Double price;

    @ManyToOne(fetch = FetchType.LAZY,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "invoice_id", referencedColumnName = "id")
    private Invoice invoice;

    @Column(nullable = false)
    private String SKU;

    @Column(nullable = false)
    private String name;

    @NotNull
    private Integer quantity;

}
