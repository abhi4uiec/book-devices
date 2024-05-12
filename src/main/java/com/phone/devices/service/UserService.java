package com.phone.devices.service;

import com.phone.devices.domain.user.UserRequestDTO;
import com.phone.devices.domain.user.UserResponseDTO;

public interface UserService {

    UserResponseDTO create(final UserRequestDTO req);
}
