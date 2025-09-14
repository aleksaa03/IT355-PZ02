package com.it355.movie_management.security.aspects;

import com.it355.movie_management.dtos.UserPayload;
import com.it355.movie_management.exceptions.ForbiddenException;
import com.it355.movie_management.security.annotations.RoleSecured;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Arrays;

@Aspect
@Component
public class RoleSecuredAspect {
    @Before("@annotation(roleSecured)")
    public void checkRole(JoinPoint joinPoint, RoleSecured roleSecured) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            throw new RuntimeException("No request context");
        }

        HttpServletRequest request = attributes.getRequest();
        UserPayload currentUser = (UserPayload) request.getAttribute("currentUser");
        if (currentUser == null) {
            throw new RuntimeException("Not authorized");
        }

        boolean allowed = Arrays.stream(roleSecured.value())
                .anyMatch(role -> role.getValue() == currentUser.roleId());

        if (!allowed) {
            throw new ForbiddenException("Forbidden");
        }
    }
}