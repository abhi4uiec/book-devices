package com.phone.devices.controller;

import com.phone.devices.domain.Phone;
import com.phone.devices.exception.PhoneNotAvailableException;
import com.phone.devices.exception.PhoneNotFoundException;
import com.phone.devices.exception.PhoneNotReturnedException;
import com.phone.devices.service.PhoneService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/phones/")
public class PhoneController {

    private final PhoneService phoneService;

    public PhoneController(final PhoneService phoneService) {
        this.phoneService = phoneService;
    }

    @Operation(summary = "Get information about the phone model")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Phone info returned successfully",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = Phone.class)) }),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request",
                    content = {@Content(schema = @Schema(oneOf = {
                            PhoneNotAvailableException.class,
                            PhoneNotReturnedException.class
                    }) )}),
            @ApiResponse(
                    responseCode = "404",
                    description = "Phone not supported",
                    content = {@Content(schema = @Schema(oneOf = {
                            PhoneNotFoundException.class,
                    }) )}),
            @ApiResponse(
                    responseCode = "401",
                    description = "You are not authorized")
    })
    /**
     * Retrieves a phone by its model.
     *
     * @param model The model of the phone to retrieve.
     * @return The phone with the specified model.
     */
    @GetMapping("/{model}")
    public Phone getPhone(final @PathVariable String model) {
        return phoneService.getPhone(model, true);
    }

    @Operation(summary = "Allows a phone to be booked")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Phone booked successfully",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = Phone.class)) }),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request",
                    content = {@Content(schema = @Schema(oneOf = {
                            PhoneNotAvailableException.class,
                            PhoneNotReturnedException.class
                    }) )}),
            @ApiResponse(
                    responseCode = "404",
                    description = "Phone not supported",
                    content = {@Content(schema = @Schema(oneOf = {
                            PhoneNotFoundException.class,
                    }) )}),
            @ApiResponse(
                    responseCode = "401",
                    description = "You are not authorized to book a phone")
    })
    /**
     * Books a phone by its model for a specific user.
     *
     * @param model     The model of the phone to book.
     * @param bookedBy  The user who is booking the phone.
     * @return The phone that has been booked.
     */
    @PostMapping("/{model}/book")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')") // only users with admin role can book phone
    public Phone bookPhone(final @PathVariable String model, final @RequestParam @Valid String bookedBy) {
        return phoneService.bookPhone(model, bookedBy);
    }

    @Operation(summary = "Allows a phone to be returned")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Phone returned successfully",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = Phone.class)) }),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request",
                    content = {@Content(schema = @Schema(oneOf = {
                            PhoneNotAvailableException.class,
                            PhoneNotReturnedException.class
                    }) )}),
            @ApiResponse(
                    responseCode = "404",
                    description = "Phone not supported",
                    content = { @Content(schema = @Schema(oneOf = {
                            PhoneNotFoundException.class,
                    }) )}),
            @ApiResponse(
                    responseCode = "401",
                    description = "You are not authorized")
    })
    /**
     * Returns a booked phone by its model.
     *
     * @param model The model of the phone to return.
     * @return The phone that has been returned.
     */
    @PutMapping("/{model}/return")
    public Phone returnPhone(final @PathVariable String model) {
        return phoneService.returnPhone(model);
    }

}