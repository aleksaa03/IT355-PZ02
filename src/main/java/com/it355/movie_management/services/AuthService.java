package com.it355.movie_management.services;

import com.it355.movie_management.common.enums.UserRole;
import com.it355.movie_management.dtos.auth.LoginUserResponseDto;
import com.it355.movie_management.exceptions.ConflictException;
import com.it355.movie_management.exceptions.NotAuthorizedException;
import com.it355.movie_management.exceptions.NotFoundException;
import com.it355.movie_management.models.User;
import com.it355.movie_management.repositories.UserRepository;
import com.it355.movie_management.utils.JWTUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public User register(String username, String password) {
        if (userRepository.existsByUsername(username)) {
            throw new ConflictException("User already exists.");
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setRoleId(UserRole.Client);

        return userRepository.save(user);
    }

    public LoginUserResponseDto login(String username, String password, HttpServletResponse response) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new NotFoundException("User don't exists in the system.");
        }

        boolean matches = passwordEncoder.matches(password, user.getPassword());
        if (!matches) {
            throw new NotAuthorizedException("Invalid credentials.");
        }

        String token = JWTUtil.generateToken(user.getId(), user.getUsername(), user.getRoleId().ordinal());

        Cookie cookie = new Cookie("token", token);
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setMaxAge(24 * 60 * 60);
        response.addCookie(cookie);

        return new LoginUserResponseDto(user.getId(), user.getUsername(), user.getRoleId());
    }
}