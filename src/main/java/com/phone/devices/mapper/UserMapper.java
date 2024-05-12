package com.phone.devices.mapper;

import com.phone.devices.domain.user.UserRequestDTO;
import com.phone.devices.domain.user.UserResponseDTO;
import com.phone.devices.entity.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class UserMapper {
    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public static User toEntity(final UserRequestDTO dto) {
        return User.builder()
                .username(dto.getUsername())
                .password(encoder.encode(dto.getPassword()))
                .authority(dto.getAuthority())
                .build();
    }

    public static UserResponseDTO toResponse(final User entity) {
        return UserResponseDTO.builder()
                .username(entity.getUsername())
                .authority(entity.getAuthority())
                .build();
    }
}
