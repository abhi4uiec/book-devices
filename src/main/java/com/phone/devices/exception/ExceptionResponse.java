package com.phone.devices.exception;

import org.springframework.http.HttpStatus;

public record ExceptionResponse(String message, HttpStatus status) { }
