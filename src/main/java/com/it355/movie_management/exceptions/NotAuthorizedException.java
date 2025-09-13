package com.it355.movie_management.exceptions;

import com.it355.movie_management.exceptions.abstraction.CustomException;

public class NotAuthorizedException extends CustomException {
    public NotAuthorizedException() {
        this("Not authorized.");
    }

    public NotAuthorizedException(String message) {
        super(message, 401);
    }
}