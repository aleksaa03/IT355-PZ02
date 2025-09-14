package com.it355.movie_management.exceptions;

import com.it355.movie_management.exceptions.abstraction.CustomException;

public class ForbiddenException extends CustomException {
    public ForbiddenException() {
        this("Forbidden.");
    }

    public ForbiddenException(String message) {
        super(message, 403);
    }
}