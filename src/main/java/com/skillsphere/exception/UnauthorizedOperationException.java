package com.skillsphere.exception;

public class UnauthorizedOperationException extends ApplicationException {

    public UnauthorizedOperationException(String message) {
        super(message);
    }
}