package com.it355.movie_management.exceptions;

import com.it355.movie_management.exceptions.abstraction.CustomException;

public class ConflictException extends CustomException {
    public ConflictException() {
        this("Conflict");
    }

    public ConflictException(String message) {
        super(message, 409);
    }
}