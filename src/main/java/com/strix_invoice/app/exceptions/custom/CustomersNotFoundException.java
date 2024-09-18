/**
 * The {@code CustomersNotFoundException$} class represents Functionalities
 *
 * @author ArunKumar D
 */

package com.strix_invoice.app.exceptions.custom;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class CustomersNotFoundException extends RuntimeException {
    public CustomersNotFoundException(String message) {
        super(message);
    }
}
