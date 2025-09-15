package com.it355.movie_management.controllers;

import com.it355.movie_management.common.enums.UserRole;
import com.it355.movie_management.dtos.Pagination;
import com.it355.movie_management.dtos.user_log.UserLogDto;
import com.it355.movie_management.security.annotations.RoleSecured;
import com.it355.movie_management.services.UserLogService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user-logs")
public class UserLogController {
    private final UserLogService userLogService;

    public UserLogController(UserLogService userLogService) {
        this.userLogService = userLogService;
    }

    @GetMapping()
    @RoleSecured({UserRole.Admin})
    public ResponseEntity<?> getList(Pagination pagination) {
        Page<UserLogDto> userLogList = userLogService.getList(pagination);

        Map<String, Object> response = new HashMap<>();
        response.put("userLogs", userLogList.getContent());
        response.put("total", userLogList.getTotalElements());
        response.put("totalPages", userLogList.getTotalPages());

        return ResponseEntity.ok(response);
    }
}