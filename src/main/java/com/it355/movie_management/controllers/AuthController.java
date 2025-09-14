package com.it355.movie_management.controllers;

import com.it355.movie_management.dtos.UserPayload;
import com.it355.movie_management.dtos.auth.LoginUserResponseDto;
import com.it355.movie_management.dtos.auth.RegisterUserResponseDto;
import com.it355.movie_management.dtos.user.UserDto;
import com.it355.movie_management.exceptions.BadRequestException;
import com.it355.movie_management.services.AuthService;
import com.it355.movie_management.utils.JWTUtil;
import com.it355.movie_management.utils.StringUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class AuthController {
    private final AuthService authService;
    private final JWTUtil jWTUtil;

    public AuthController(AuthService authService, JWTUtil jWTUtil) {
        this.authService = authService;
        this.jWTUtil = jWTUtil;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        String password = body.get("password");

        if (StringUtil.isNullOrEmpty(username) || StringUtil.isNullOrEmpty(password))
            throw new BadRequestException("Username or password missing");

        if (username.length() < 5 || password.length() < 5)
            throw new BadRequestException("Username and password must be minimun 5 characters long.");

        RegisterUserResponseDto registeredUser = authService.register(username, password);

        return ResponseEntity.status(201).body(Map.of(
                "user", Map.of(
                        "id", registeredUser.getId(),
                        "username", registeredUser.getUsername(),
                        "roleId", registeredUser.getRoleId().ordinal()
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
                        "roleId", loggedUser.getRoleId().ordinal()
                ),
                "message", "Login successful."
        ));
    }

    @DeleteMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        authService.logout(response);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/check-auth")
    public ResponseEntity<?> checkAuth(HttpServletRequest request) {
        UserPayload currentUser = (UserPayload) request.getAttribute("currentUser");
        UserDto user = authService.getUserById(currentUser.id());

        return ResponseEntity.ok(Map.of("user", Map.of(
                "id", user.getId(),
                "username", user.getUsername(),
                "roleId", user.getRoleId()
        )));
    }
}