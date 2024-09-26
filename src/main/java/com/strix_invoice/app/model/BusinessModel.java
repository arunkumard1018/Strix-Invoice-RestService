/**
 * The {@code BusinessModel$} class represents Functionalities
 *
 * @author ArunKumar D
 */

package com.strix_invoice.app.model;

import com.strix_invoice.app.records.BusinessType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class BusinessModel {
    private Long id;

    @NotBlank(message = "Business Name Can't Be Blank")
    @Size(max = 50)
    private String name;

    private String gstin;
    private Integer hsn;
    private Integer stateCode;
    private BusinessType businessType;
    @Size(max = 15)
    private String invoicePrefix;
    private String businessLogo;
    @NotBlank(message = "AddressModel Required")
    private String address;
    @NotBlank(message = "City Required")
    private String city;
    @NotBlank(message = "sate Required")
    private String state;
    @NotNull(message = "Zip Code Required")
    private Integer zip;
}
