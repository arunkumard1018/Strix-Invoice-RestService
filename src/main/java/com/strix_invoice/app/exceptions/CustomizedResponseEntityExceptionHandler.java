/**
 * The {@code CustomizedResponseEntityExceptionHandler$} class represents Functionalities
 *
 * @author ArunKumar D
 */

package com.strix_invoice.app.exceptions;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.strix_invoice.app.exceptions.custom.UserAlreadyExistsException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.lang.Nullable;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@ControllerAdvice
public class CustomizedResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        log.info("Validation failed: {} error(s)", ex.getErrorCount());

        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.toList());

        log.debug("Validation errors: {}", errors);

        ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(), "Validation failed", errors.toString());
        errorDetails.setStatus(HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @Nullable
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        log.warn("JSON parse error: Failed to read the request");

        Throwable rootCause = ex.getMostSpecificCause();
        String errorMessage;

        if (rootCause instanceof com.fasterxml.jackson.databind.exc.InvalidFormatException) {
            // Cast the root cause to InvalidFormatException
            com.fasterxml.jackson.databind.exc.InvalidFormatException invalidFormatException = (com.fasterxml.jackson.databind.exc.InvalidFormatException) rootCause;

            // Extracting field path and invalid value
            String fieldName = invalidFormatException.getPath().stream()
                    .map(reference -> reference.getFieldName())  // Only get the field name
                    .reduce((first, second) -> second)           // Get the last field name in the path (most relevant)
                    .orElse("unknown field");

            Object invalidValue = invalidFormatException.getValue();
            String targetType = invalidFormatException.getTargetType().getSimpleName();

            // Constructing a clean error message
            errorMessage = String.format("Invalid value '%s' for field '%s'. Expected type: %s.", invalidValue, fieldName, targetType);
        } else {
            // Default error message for other causes
            errorMessage = rootCause != null ? rootCause.getMessage() : ex.getMessage();
        }

        // Log and return the detailed error response
        ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(), "Failed to read request", errorMessage);
        errorDetails.setStatus(HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }


}