package com.it355.movie_management.exceptions;

import com.it355.movie_management.exceptions.abstraction.CustomException;

public class BadRequestException extends CustomException {
    public BadRequestException() {
        this("Bad request.");
    }

    public BadRequestException(String message) {
        super(message, 400);
    }
}