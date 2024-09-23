/**
 * The {@code ErrorDetails$} class represents Functionalities
 *
 * @author ArunKumar D
 */

package com.strix_invoice.app.exceptions;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

/**
 * Custom Error Details for the Application
 * <p>
 * Note : By default Spring uses ResponseEntityExceptionHandler class for Error structure
 * this is standard class which handles all spring mvc raised exceptions and it returns formated error details
 */

@Setter
@Getter
public class ErrorDetails {

    private LocalDateTime timeStamp;
    private String message;
    private String details;
    private Integer status;

    public ErrorDetails(LocalDateTime timeStamp, String message, String details) {
        super();
        this.timeStamp = timeStamp;
        this.message = message;
        this.details = details;
    }

    public void setStatus(HttpStatus status) {
        this.status = status.value();
    }
}