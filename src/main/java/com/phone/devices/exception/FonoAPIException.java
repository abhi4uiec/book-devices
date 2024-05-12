package com.phone.devices.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class FonoAPIException extends RuntimeException {

    public FonoAPIException(final String message) {
        super(message);
    }
}