package com.it355.movie_management.exceptions;

import com.it355.movie_management.exceptions.abstraction.CustomException;

public class NotFoundException extends CustomException {
    public NotFoundException() {
        this("Not found.");
    }

    public NotFoundException(String message) {
        super(message, 404);
    }
}