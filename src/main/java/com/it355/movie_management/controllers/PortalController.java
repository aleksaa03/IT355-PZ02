package com.it355.movie_management.controllers;

import com.it355.movie_management.common.enums.UserLogType;
import com.it355.movie_management.dtos.UserPayload;
import com.it355.movie_management.services.UserLogService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class PortalController {
    @Autowired
    private HttpServletRequest request;

    @Autowired
    private UserLogService userLogService;

    protected UserPayload currentUser() {
        return (UserPayload) request.getAttribute("currentUser");
    }

    protected void addUserLog(UserLogType action) {
        addUserLog(action, null, null);
    }

    protected void addUserLog(UserLogType action, String description) {
        addUserLog(action, description, null);
    }

    protected void addUserLog(Long userId, UserLogType action, String description, String details) {
        userLogService.addUserLog(userId, action, description, details);
    }

    protected void addUserLog(UserLogType action, String description, String details) {
        userLogService.addUserLog(this.currentUser().id(), action, description, details);
    }
}