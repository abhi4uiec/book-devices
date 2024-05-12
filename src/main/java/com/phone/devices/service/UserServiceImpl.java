package com.phone.devices.service;

import com.phone.devices.domain.user.UserRequestDTO;
import com.phone.devices.domain.user.UserResponseDTO;
import com.phone.devices.entity.User;
import com.phone.devices.mapper.UserMapper;
import com.phone.devices.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository repository;

    /**
     * Creates a new user based on the provided user request DTO.
     *
     * @param req The user request DTO containing user data.
     * @return A user response DTO containing the details of the created user.
     */
    @Override
    public UserResponseDTO create(final UserRequestDTO req) {
        final User user = UserMapper.toEntity(req);
        final User result = this.repository.save(user);
        return UserMapper.toResponse(result);
    }
}
