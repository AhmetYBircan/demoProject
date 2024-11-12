package com.ayb.demo.models;


import java.util.Date;

import jakarta.persistence.*;

@Entity 
@Table(name = "users")
public class User {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String name;
    private String email;
    private String password;
    public String getPassword() {
        return password;
    }

 
    public void setPassword(String password) {
        this.password = password;
    }


    private String company;
    public int getId() {
        return id;
    }


    public void setId(int id) {
        this.id = id;
    }


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


    public Date getCreatedAt() {
        return createdAt;
    }


    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }


    private String country;

  
    @Column(columnDefinition = "TEXT")
    private Date  createdAt;


}