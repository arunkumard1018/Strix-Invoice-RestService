/**
 * The {@code CustomersModel$} class represents Functionalities
 *
 * @author ArunKumar D
 */

package com.strix_invoice.app.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.strix_invoice.app.Entity.Address;
import com.strix_invoice.app.Entity.Business;
import com.strix_invoice.app.Entity.UsersInfo;
import jakarta.persistence.*;
import lombok.Data;

@Data
public class CustomersModel {

    private Long id;
    private String name;
    private Long phone;
    private String email;
    private String pan;
    private String gstin;
    private AddressModel billingAddress;
    private AddressModel shippingAddress;

}
