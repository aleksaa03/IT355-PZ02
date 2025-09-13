package com.it355.movie_management.dtos.auth;

import com.it355.movie_management.common.enums.UserRole;

public class LoginUserResponseDto {
    private Long id;
    private String username;
    private UserRole roleId;

    public LoginUserResponseDto() {}

    public LoginUserResponseDto(Long id, String username, UserRole roleId) {
        this.id = id;
        this.username = username;
        this.roleId = roleId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public UserRole getRoleId() {
        return roleId;
    }

    public void setRoleId(UserRole roleId) {
        this.roleId = roleId;
    }
}