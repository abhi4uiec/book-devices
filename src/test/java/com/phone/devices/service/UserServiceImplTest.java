package com.phone.devices.service;

import com.phone.devices.domain.user.UserRequestDTO;
import com.phone.devices.domain.user.UserResponseDTO;
import com.phone.devices.entity.User;
import com.phone.devices.repo.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("it")
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserServiceImpl(userRepository);
    }

    @Test
    void testCreateUser() {
        // Arrange
        UserRequestDTO requestDTO = new UserRequestDTO("test@example.com", "Password123", "ROLE_USER");
        User user = new User(1L, "test@example.com", "encodedPassword", "ROLE_USER");
        UserResponseDTO expectedResponseDTO = new UserResponseDTO("test@example.com", "ROLE_USER");

        // Mocking UserRepository save method
        when(userRepository.save(any(User.class))).thenReturn(user);

        // Act
        UserResponseDTO actualResponseDTO = userService.create(requestDTO);

        // Assert
        assertEquals(expectedResponseDTO.authority(), actualResponseDTO.authority());
        assertEquals(expectedResponseDTO.username(), actualResponseDTO.username());
    }
}

