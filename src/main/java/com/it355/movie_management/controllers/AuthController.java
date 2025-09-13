package com.it355.movie_management.controllers;

import com.it355.movie_management.dtos.auth.LoginUserResponseDto;
import com.it355.movie_management.exceptions.BadRequestException;
import com.it355.movie_management.models.User;
import com.it355.movie_management.services.AuthService;
import com.it355.movie_management.utils.StringUtil;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        String password = body.get("password");

        if (StringUtil.isNullOrEmpty(username) || StringUtil.isNullOrEmpty(password))
            throw new BadRequestException("Username or password missing");

        if (username.length() < 5 || password.length() < 5)
            throw new BadRequestException("Username and password must be minimun 5 characters long.");

        User savedUser = authService.register(username, password);

        return ResponseEntity.status(201).body(Map.of(
                "user", Map.of(
                        "id", savedUser.getId(),
                        "username", savedUser.getUsername(),
                        "roleId", savedUser.getRoleId()
                ),
                "message", "User created successfully"
        ));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body, HttpServletResponse response) {
        String username = body.get("username");
        String password = body.get("password");

        if (StringUtil.isNullOrEmpty(username) || StringUtil.isNullOrEmpty(password))
            throw new BadRequestException("Username or password missing");

        if (username.length() < 5 || password.length() < 5)
            throw new BadRequestException("Username and password must be minimun 5 characters long.");

        LoginUserResponseDto loggedUser = authService.login(username, password, response);

        return ResponseEntity.status(200).body(Map.of(
                "user", Map.of(
                        "id", loggedUser.getId(),
                        "username", loggedUser.getUsername(),
                        "roleId", loggedUser.getRoleId()
                ),
                "message", "Login successful."
        ));
    }
}