package com.phone.devices.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

// Global exception handler
@RestControllerAdvice
public class PhoneExceptionHandler {

    @ExceptionHandler(PhoneNotAvailableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public final ResponseEntity handleUnavailablePhone(final PhoneNotAvailableException ex) {
        ExceptionResponse response = new ExceptionResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
        return ResponseEntity.status(response.status()).body(response);
    }

    @ExceptionHandler(PhoneNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public final ResponseEntity handlePhoneNotFoundException(final PhoneNotFoundException ex) {
        ExceptionResponse response = new ExceptionResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
        return ResponseEntity.status(response.status()).body(response);
    }

    @ExceptionHandler(PhoneNotReturnedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public final ResponseEntity handlePhoneNotReturnedException(final PhoneNotReturnedException ex) {
        ExceptionResponse response = new ExceptionResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
        return ResponseEntity.status(response.status()).body(response);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public final ResponseEntity handleException(final Exception ex) {
        ExceptionResponse response = new ExceptionResponse(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        return ResponseEntity.status(response.status()).body(response);
    }
}
