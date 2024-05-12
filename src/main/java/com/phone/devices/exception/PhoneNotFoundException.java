package com.phone.devices.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class PhoneNotFoundException  extends RuntimeException {

    public PhoneNotFoundException(final String message) {
        super(message);
    }
}