package com.it355.movie_management.controllers;

import com.it355.movie_management.common.enums.UserRole;
import com.it355.movie_management.security.annotations.RoleSecured;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {
    @PostMapping()
    @RoleSecured({UserRole.Admin})
    public ResponseEntity<?> createUser() {
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("message", "User created successfully."));
    }
}