/**
 * The {@code BusinessModel$} class represents Functionalities
 *
 * @author ArunKumar D
 */

package com.strix_invoice.app.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BusinessModel {
    private Long id;
    @NotBlank(message = "Business Name Can't Be Blank")
    private String name;
    private String gstin;
    private Integer hsn;
    private Integer stateCode;
    @NotBlank(message = "Address Required")
    private String address;
    @NotBlank(message = "City Required")
    private String city;
    @NotBlank(message = "sate Required")
    private String state;
    @NotNull(message = "Zip Code Required")
    private Integer zip;
}
