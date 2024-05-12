package com.phone.devices.controller;

import com.phone.devices.domain.Phone;
import com.phone.devices.exception.PhoneNotAvailableException;
import com.phone.devices.exception.PhoneNotFoundException;
import com.phone.devices.exception.PhoneNotReturnedException;
import com.phone.devices.service.PhoneService;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("it")
class PhoneControllerTest {

    @Mock
    private PhoneService phoneService;

    private PhoneController phoneController;

    private final static String VALID_DEVICE = "TestModel";

    private final static String BOOKED_BY = "testUser";

    @BeforeEach
    void setUp() {
        phoneController = new PhoneController(phoneService);
        RestAssuredMockMvc.standaloneSetup(phoneController);
    }

    @Test
    void testGetPhone_Success() {
        // Mock service method to return a phone
        Phone expectedPhone = new Phone(VALID_DEVICE);
        when(phoneService.getPhone(VALID_DEVICE, true)).thenReturn(expectedPhone);

        // Call the controller method and verify the response
        given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .get("/phones/TestModel")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("model", equalTo(VALID_DEVICE));
    }

    @Test
    void testGetPhone_PhoneNotFoundException() {
        // Mock service method to throw PhoneNotFoundException
        when(phoneService.getPhone("NonExistentModel", true)).thenThrow(new PhoneNotFoundException("Phone not found"));

        // Call the controller method and expect an error response
        given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .get("/phones/NonExistentModel")
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void testBookPhone_Success() {
        // Mock service method to return a booked phone
        Phone expectedPhone = new Phone(VALID_DEVICE);
        when(phoneService.bookPhone(VALID_DEVICE, BOOKED_BY)).thenReturn(expectedPhone);

        // Call the controller method and verify the response
        given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .param("bookedBy", BOOKED_BY)
                .post("/phones/TestModel/book")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("model", equalTo(VALID_DEVICE));
    }

    @Test
    void testBookPhone_PhoneNotAvailableException() {
        // Mock service method to return a booked phone
        Phone expectedPhone = new Phone(VALID_DEVICE);
        when(phoneService.bookPhone(VALID_DEVICE, BOOKED_BY)).thenThrow(new PhoneNotAvailableException("Phone not available"));

        // Call the controller method and verify the response
        given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .param("bookedBy", BOOKED_BY)
                .post("/phones/TestModel/book")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void testReturnPhone_Success() {
        // Mock service method to return a phone
        Phone expectedPhone = new Phone(VALID_DEVICE);
        when(phoneService.returnPhone(VALID_DEVICE)).thenReturn(expectedPhone);

        // Call the controller method and verify the response
        given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .put("/phones/TestModel/return")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("model", equalTo(VALID_DEVICE));
    }

    @Test
    void testReturnPhone_PhoneNotReturnedException() {
        // Mock service method to throw PhoneNotReturnedException
        when(phoneService.returnPhone(anyString())).thenThrow(new PhoneNotReturnedException("Phone not returned"));

        // Call the controller method and expect an error response
        given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .put("/phones/NonExistentModel/return")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

}

