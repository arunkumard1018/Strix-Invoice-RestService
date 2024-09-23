/**
 * The {@code InvalidRequestBodyException$} class represents Functionalities
 *
 * @author ArunKumar D
 */

package com.strix_invoice.app.exceptions.custom;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidRequestBodyException extends RuntimeException{
    public InvalidRequestBodyException(String message){
        super(message);
    }
}
