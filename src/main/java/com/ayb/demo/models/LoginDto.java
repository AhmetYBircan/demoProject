package com.ayb.demo.models;

import jakarta.validation.constraints.*;
import jakarta.validation.constraints.NotEmpty;

public class LoginDto {

    @NotEmpty(message = "User email is required")
    @Email
    private String email;

    @NotEmpty(message = "User password is required")
    private String password;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
