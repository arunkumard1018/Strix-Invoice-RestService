/**
 * The {@code RegisterUserInfo$} class represents Functionalities
 *
 * @author ArunKumar D
 */

package com.strix_invoice.app.model;

import com.strix_invoice.app.records.BusinessType;
import lombok.Data;

@Data
public class RegisterUserInfo {
    private String name;
    private String email;
    private String password;
    private String city;
    private String state;
    private Integer zip;
    private boolean isAgreedThePolicy;
    private String businessName;
    private BusinessType businessType;

}