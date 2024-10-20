package com.ecommerce.core.service.impl;

import com.ecommerce.core.dto.request.OrderDTO;
import com.ecommerce.core.dto.response.UserDTO;
import com.ecommerce.core.mapper.UserMapper;
import com.ecommerce.core.repository.UserRepository;
import com.ecommerce.core.service.UserService;
import com.ecommerce.core.entity.User;
import java.security.Principal;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public User getCurrentUser(UserDTO userDTO) {
        // Get the current authenticated user (if any)
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info("Authentication: {}", authentication);
        log.info("Authentication principal: {}", authentication.getPrincipal());
        // Check if the user is authenticated (meaning logged in via Keycloak)
        if (Objects.nonNull(authentication) && authentication.getPrincipal() instanceof Jwt) {
            // Extract user details from authentication object
            Jwt jwt = (Jwt) authentication.getPrincipal();
            // Extract the user's email from the JWT claims (or any other relevant field)
            String email = jwt.getClaim("email");  // Adjust the claim name as per your JWT structure

            // If the user is registered (exists in DB), grab him
            if (userRepository.existsByEmail(email)) {
                Optional<User> registeredUser = userRepository.findByEmail(email);
                return registeredUser.orElseThrow(() -> new UsernameNotFoundException("User not found"));

            } else {
                // If not in DB, add the user to the database
                User newUser = userMapper.userDTOToUser(userDTO);
                newUser.setRegistered(true);
                return userRepository.save(newUser);
            }

        } else {
            // If no authentication, the user is not registered
            User dtoUser = userMapper.userDTOToUser(userDTO);
            String email = dtoUser.getEmail();
            // If the user exists in DB, grab him
            if (userRepository.existsByEmail(email)) {
                Optional<User> existingUser = userRepository.findByEmail(email);
                return existingUser.orElseThrow(() -> new UsernameNotFoundException("User not found"));

            } else {
                // If not in DB, add the user
                return userRepository.save(dtoUser);
            }
        }
    }
}
