package com.phone.devices.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class PhoneNotReturnedException  extends RuntimeException {

    public PhoneNotReturnedException(final String message) {
        super(message);
    }
}
