package com.it355.movie_management.dtos.auth;

import com.it355.movie_management.common.enums.UserRole;

public class RegisterUserResponseDto extends LoginUserResponseDto {
    public RegisterUserResponseDto() {
        super();
    }

    public RegisterUserResponseDto(Long id, String username, UserRole roleId) {
        super(id, username, roleId);
    }
}