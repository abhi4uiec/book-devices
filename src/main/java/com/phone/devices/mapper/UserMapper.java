package com.phone.devices.mapper;

import com.phone.devices.domain.user.UserRequest;
import com.phone.devices.domain.user.UserResponse;
import com.phone.devices.entity.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class UserMapper {
    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public static User toEntity(final UserRequest dto) {
        return User.builder()
                .username(dto.username())
                .password(encoder.encode(dto.password()))
                .authority(dto.authority())
                .build();
    }

    public static UserResponse toResponse(final User entity) {
        return new UserResponse(entity.getUsername(), entity.getAuthority());
    }
}
