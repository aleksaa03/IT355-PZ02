package com.it355.movie_management.controllers;

import com.it355.movie_management.common.enums.UserLogType;
import com.it355.movie_management.dtos.auth.LoginUserResponseDto;
import com.it355.movie_management.dtos.auth.RegisterUserResponseDto;
import com.it355.movie_management.dtos.user.UserDto;
import com.it355.movie_management.exceptions.BadRequestException;
import com.it355.movie_management.services.AuthService;
import com.it355.movie_management.utils.StringUtil;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class AuthController extends PortalController {
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

        RegisterUserResponseDto registeredUser = authService.register(username, password);

        this.addUserLog(registeredUser.getId(),
                UserLogType.Auth,
                "User registered",
                String.format("User %s registered successfully", registeredUser.getUsername()));

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

        this.addUserLog(loggedUser.getId(),
                UserLogType.Auth,
                "User logged in",
                String.format("User %s logged in successfully", loggedUser.getUsername()));

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

        this.addUserLog(UserLogType.Auth,
                "User logged out",
                String.format("User %s logged out successfully", this.currentUser().username()));

        return ResponseEntity.ok().build();
    }

    @GetMapping("/check-auth")
    public ResponseEntity<?> checkAuth() {
        UserDto user = authService.getUserById(this.currentUser().id());

        return ResponseEntity.ok(Map.of("user", Map.of(
                "id", user.getId(),
                "username", user.getUsername(),
                "roleId", user.getRoleId()
        )));
    }
}