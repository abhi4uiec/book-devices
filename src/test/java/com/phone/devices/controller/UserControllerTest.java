package com.phone.devices.controller;

import com.phone.devices.domain.user.UserRequestDTO;
import com.phone.devices.domain.user.UserResponseDTO;
import com.phone.devices.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("it")
class UserControllerTest {

    @Mock
    private UserService userService;

    private UserController userController;

    @BeforeEach
    void setUp() {
        userController = new UserController(userService);
    }

    @Test
    void createUser_ValidUserRequest_ReturnsCreatedResponse() {
        // Arrange
        UserRequestDTO requestDTO = UserRequestDTO.builder()
                .username("testuser")
                .password("TestPassword123")
                .authority("ROLE_USER")
                .build();

        UserResponseDTO expectedResponseDTO = UserResponseDTO.builder()
                .username("testuser")
                .authority("ROLE_USER")
                .build();

        when(userService.create(requestDTO)).thenReturn(expectedResponseDTO);

        // Act
        ResponseEntity<UserResponseDTO> responseEntity = userController.createUser(requestDTO);

        // Assert
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(expectedResponseDTO, responseEntity.getBody());
    }
}
