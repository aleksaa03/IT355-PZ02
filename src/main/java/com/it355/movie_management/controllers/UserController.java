package com.it355.movie_management.controllers;

import com.it355.movie_management.common.enums.UserLogType;
import com.it355.movie_management.common.enums.UserRole;
import com.it355.movie_management.dtos.Pagination;
import com.it355.movie_management.dtos.user.UserDto;
import com.it355.movie_management.exceptions.BadRequestException;
import com.it355.movie_management.security.annotations.RoleSecured;
import com.it355.movie_management.services.UserService;
import com.it355.movie_management.utils.StringUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController extends PortalController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping()
    @RoleSecured({UserRole.Admin})
    public ResponseEntity<?> createUser(@RequestBody Map<String, String> body) {
        String username =  body.get("username");
        String password = body.get("password");
        UserRole roleId = UserRole.fromNum(Integer.parseInt(body.get("roleId")));

        if (StringUtil.isNullOrEmpty(username) || StringUtil.isNullOrEmpty(password))
            throw new BadRequestException("Username or password missing");

        if (username.length() < 5 || password.length() < 5)
            throw new BadRequestException("Username and password must be minimun 5 characters long.");

        UserDto createdUser = userService.createUser(username, password, roleId);

        this.addUserLog(UserLogType.Add,
                String.format("Created user with ID %s", createdUser.getId()),
                String.format("User: %s, %s", createdUser.getId(), createdUser.getUsername()));

        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("message", "User created successfully."));
    }

    @GetMapping()
    @RoleSecured({UserRole.Admin})
    public ResponseEntity<?> getList(Pagination pagination) {
        Page<UserDto> usersList = userService.getList(pagination);

        Map<String, Object> response = new HashMap<>();
        response.put("users", usersList.getContent());
        response.put("total", usersList.getTotalElements());
        response.put("totalPages", usersList.getTotalPages());

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{userId}")
    @RoleSecured({UserRole.Admin})
    public ResponseEntity<?> updateUser(@PathVariable Long userId, @RequestBody Map<String, String> body) {
        String username = body.get("username");
        UserRole roleId = UserRole.fromNum(Integer.parseInt(body.get("roleId")));

        if (userId == null) {
            throw new BadRequestException("User id must be number.");
        }

        if (StringUtil.isNullOrEmpty(username)) {
            throw new BadRequestException("Username is empty.");
        }

        userService.updateUser(userId, username, roleId);

        this.addUserLog(UserLogType.Update, String.format("Updated user with ID %s", userId));

        return ResponseEntity.ok(Map.of("message", "User updated successfully"));
    }

    @DeleteMapping("/{userId}")
    @RoleSecured({UserRole.Admin})
    public ResponseEntity<?> deleteUser(@PathVariable Long userId, HttpServletRequest request) {
        if (userId == null) {
            throw new BadRequestException("User id must be number.");
        }

        userService.deleteUser(userId, this.currentUser().id());

        this.addUserLog(UserLogType.Delete,
                String.format("Deleted user with ID %s", userId),
                String.format("User ID: %s", userId));

        return ResponseEntity.ok(Map.of("message", "User deleted successfully."));
    }
}