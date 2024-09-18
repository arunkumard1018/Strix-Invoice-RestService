/**
 * The {@code AddressModel$} class represents Functionalities
 *
 * @author ArunKumar D
 */

package com.strix_invoice.app.model;

import lombok.Data;

@Data
public class AddressModel {
    private Long id;
    private String address;
    private String city;
    private String state;
    private Integer zip;
}
