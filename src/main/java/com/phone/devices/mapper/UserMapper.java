package com.phone.devices.mapper;

import com.phone.devices.domain.user.UserRequestDTO;
import com.phone.devices.domain.user.UserResponseDTO;
import com.phone.devices.entity.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class UserMapper {
    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public static User toEntity(final UserRequestDTO dto) {
        return User.builder()
                .username(dto.username())
                .password(encoder.encode(dto.password()))
                .authority(dto.authority())
                .build();
    }

    public static UserResponseDTO toResponse(final User entity) {
        return new UserResponseDTO(entity.getUsername(), entity.getAuthority());
    }
}
