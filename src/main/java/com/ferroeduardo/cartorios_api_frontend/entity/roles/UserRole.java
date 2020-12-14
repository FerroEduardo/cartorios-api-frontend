package com.ferroeduardo.cartorios_api_frontend.entity.roles;

public enum UserRole {

    USER("ROLE_USER"),
    ADMIN("ROLE_USER,ROLE_ADMIN");

    String role;

    UserRole(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }
}
