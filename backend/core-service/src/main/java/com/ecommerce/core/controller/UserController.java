//package com.ecommerce.core.controller;
//
//import com.ecommerce.core.dto.response.UserDTO;
//import com.ecommerce.core.entity.User;
//import com.ecommerce.core.service.ProductService;
//import com.ecommerce.core.service.UserService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.CrossOrigin;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequestMapping(value = "login", produces = MediaType.APPLICATION_JSON_VALUE)
////@CrossOrigin(origins = "http://localhost:4200")
//public class UserController {
//
//    private final UserService userService;
//
//    @Autowired
//    public UserController(ProductService productService, UserService userService) {
//        this.userService = userService;
//    }
//
//    @PostMapping
//    public ResponseEntity<User> login(@RequestBody UserDTO userDTO) {
//        return ResponseEntity.ok(userService.login(userDTO));
//    }
//}
