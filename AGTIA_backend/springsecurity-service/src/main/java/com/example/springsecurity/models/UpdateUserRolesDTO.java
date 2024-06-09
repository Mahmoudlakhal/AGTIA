package com.example.springsecurity.models;

import java.util.Set;

public class UpdateUserRolesDTO {
    private Set<ERole> roles;

    // Getters and setters
    public Set<ERole> getRoles() {
        return roles;
    }
    public void setRoles(Set<ERole> roles) {
        this.roles = roles;
    }

}
