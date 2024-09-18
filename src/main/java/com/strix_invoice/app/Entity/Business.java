/**
 * The {@code BusinessModel$} class represents Functionalities
 *
 * @author ArunKumar D
 */

package com.strix_invoice.app.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
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

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "address_id", referencedColumnName = "id")
    private Address address;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "users_info_id",referencedColumnName = "id")
    private UsersInfo usersInfo;

    @OneToMany(mappedBy = "business", fetch = FetchType.LAZY)
    private Set<Customers> customers = new HashSet<>();

}
