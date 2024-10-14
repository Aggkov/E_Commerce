package com.ecommerce.core.service;

import com.ecommerce.core.dto.response.UserDTO;
import com.ecommerce.core.entity.User;

public interface UserService {
    User register(UserDTO userDTO);
    User login(UserDTO userDTO);
}
