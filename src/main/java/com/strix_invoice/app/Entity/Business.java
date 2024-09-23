/**
 * The {@code BusinessModel$} class represents Functionalities
 *
 * @author ArunKumar D
 */

package com.strix_invoice.app.Entity;

import com.strix_invoice.app.Entity.invoice.Invoices;
import com.strix_invoice.app.records.BusinessType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Entity
@Setter
@Getter
public class Business {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String gstin;
    private Integer hsn;
    private Integer stateCode;
    private String businessLogo;

    @Enumerated(EnumType.STRING)
    private BusinessType businessType;

    private String invoicePrefix;

    private Integer invoiceSeq;


    @OneToOne(fetch = FetchType.LAZY,cascade = {CascadeType.PERSIST,CascadeType.MERGE})
    @JoinColumn(name = "address_id", referencedColumnName = "id")
    private Address address;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "users_info_id",referencedColumnName = "id")
    private UsersInfo usersInfo;

    @OneToMany(mappedBy = "business", fetch = FetchType.LAZY)
    private Set<Customers> customers = new HashSet<>();

    @OneToMany(mappedBy = "business", fetch = FetchType.LAZY)
    private List<Invoices> invoices;

}
