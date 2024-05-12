package com.phone.devices.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class PhoneNotAvailableException  extends RuntimeException {

    public PhoneNotAvailableException(final String message) {
        super(message);
    }
}