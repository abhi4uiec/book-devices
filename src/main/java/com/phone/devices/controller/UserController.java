package com.phone.devices.controller;

import com.phone.devices.domain.user.UserRequestDTO;
import com.phone.devices.domain.user.UserResponseDTO;
import com.phone.devices.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    @Operation(summary = "Creates a new user")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "User created successfully",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = UserRequestDTO.class)) }),
            @ApiResponse(
                    responseCode = "401",
                    description = "You are not authorized to create an user"),
            @ApiResponse(
                    responseCode = "500",
                    description = "Application failed to process the request"),
            })
    /**
     * Creates a new user.
     *
     * @param req The request body containing user data.
     * @return A ResponseEntity containing the created user's response DTO with HTTP status CREATED.
     */
    @PostMapping
    public ResponseEntity<UserResponseDTO> createUser(final @Valid @RequestBody UserRequestDTO req) {
        return new ResponseEntity<>(service.create(req), HttpStatus.CREATED);
    }
}
