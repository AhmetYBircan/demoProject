package com.ayb.demo.models;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public class RegisterDto {
        @NotEmpty(message = "User name is required")
        private String name;

        @NotEmpty(message = "User email is required")
        private String email;

        @NotEmpty(message = "User password is required")
        @Size(min = 5, message = "Password must be at least 5 characters long")
        @Size(max = 10, message = "Password must be less than 10 characters long")
        private String password;

        @NotEmpty(message = "User company is required")
        private String company;
        
        @NotEmpty(message = "User country is required")
        private String country;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

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

        public String getCompany() {
            return company;
        }

        public void setCompany(String company) {
            this.company = company;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }
}
