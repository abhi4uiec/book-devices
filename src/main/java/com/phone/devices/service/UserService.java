package com.phone.devices.service;

import com.phone.devices.domain.user.UserRequest;
import com.phone.devices.domain.user.UserResponse;

public interface UserService {

    UserResponse create(final UserRequest req);
}
