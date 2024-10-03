package com.me.ecommerce.service;

import com.me.ecommerce.dto.response.UserDTO;
import com.me.ecommerce.entity.User;

public interface UserService {
    User register(UserDTO userDTO);
    User login(UserDTO userDTO);
}
