package com.it355.movie_management.services;

import com.it355.movie_management.common.enums.UserRole;
import com.it355.movie_management.dtos.Pagination;
import com.it355.movie_management.dtos.user.UserDto;
import com.it355.movie_management.exceptions.BadRequestException;
import com.it355.movie_management.exceptions.ConflictException;
import com.it355.movie_management.exceptions.NotFoundException;
import com.it355.movie_management.models.User;
import com.it355.movie_management.repositories.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public UserDto createUser(String username, String password, UserRole roleId) {
        if (userRepository.existsByUsername(username)) {
            throw new ConflictException("User already exists.");
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setRoleId(roleId);

        userRepository.save(user);

        return new UserDto(user.getId(), user.getUsername(), user.getRoleId());
    }

    public Page<UserDto> getList(Pagination pagination) {
        Sort sort = Sort.by(Sort.Direction.fromString(pagination.getSortOrd()), pagination.getSortExp());
        Pageable pageable = PageRequest.of(pagination.getPage() - 1, pagination.getPageSize(), sort);

        return userRepository.findAll(pageable)
                .map((item) -> new UserDto(item.getId(), item.getUsername(), item.getRoleId()));
    }

    public void updateUser(Long userId, String username, UserRole roleId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User don't exists in the system."));

        if (!user.getUsername().equals(username) && userRepository.existsByUsername(username)) {
            throw new ConflictException("User already exists.");
        }

        user.setUsername(username);
        user.setRoleId(roleId);

        userRepository.save(user);
    }

    public void deleteUser(Long userIdForDelete, Long currentUserId) {
        if (Objects.equals(userIdForDelete, currentUserId)) {
            throw new BadRequestException("Cannot delete yourself.");
        }

        if (!userRepository.existsById(userIdForDelete)) {
            throw new NotFoundException("User don't exists in the system.");
        }

        userRepository.deleteById(userIdForDelete);
    }
}