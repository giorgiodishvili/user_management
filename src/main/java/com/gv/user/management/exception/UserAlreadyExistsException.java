package com.gv.user.management.exception;

import java.io.Serial;

public class UserAlreadyExistsException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 8177101278110608921L;

    public UserAlreadyExistsException() {
        super("Email already exists");
    }
}
