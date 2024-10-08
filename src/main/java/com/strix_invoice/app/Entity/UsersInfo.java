/**
 * The {@code UsersInfo$} class represents Functionalities
 *
 * @author ArunKumar D
 */

package com.strix_invoice.app.Entity;


import com.strix_invoice.app.Entity.invoice.Invoices;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Entity
@Setter
@Getter
public class UsersInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String name;
    private Long phone;
    private String PAN;
    private String profileImageUri;
    private String city;
    private String state;
    private Integer zip;
    private boolean isAgreedTheTerms;

    @OneToOne(fetch = FetchType.LAZY,cascade = {CascadeType.PERSIST,CascadeType.MERGE})
    @JoinColumn(name = "active_business", referencedColumnName = "id")
    private Business activeBusiness;

    // One UsersInfo can have many Businesses

    @OneToMany(mappedBy = "usersInfo", fetch = FetchType.LAZY)
    private Set<Business> businesses = new HashSet<>();

    @OneToMany(mappedBy = "usersInfo", fetch = FetchType.LAZY)
    private List<Invoices> invoices = new ArrayList<>();

    @OneToMany(mappedBy = "usersInfo", fetch = FetchType.LAZY)
    private Set<Customers> customers = new HashSet<>();

}

