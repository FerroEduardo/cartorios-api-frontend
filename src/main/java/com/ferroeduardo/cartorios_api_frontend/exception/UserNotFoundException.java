package com.ferroeduardo.cartorios_api_frontend.exception;

public class UserNotFoundException extends RuntimeException {

    private final String username;
    private final Long id;

    public UserNotFoundException(String username) {
        super(String.format("User '%s' not found", username));
        this.username = username;
        this.id = null;
    }

    public UserNotFoundException(Long id) {
        super(String.format("User with ID:'%d' not found", id));
        this.username = null;
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public Long getId() {
        return id;
    }
}
