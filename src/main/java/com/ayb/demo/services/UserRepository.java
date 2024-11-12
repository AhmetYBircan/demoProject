package com.ayb.demo.services;

import org.springframework.data.jpa.repository.JpaRepository;
import com.ayb.demo.models.User;

public interface UserRepository extends JpaRepository<User, Integer> {

    public User findByEmail(String email);
}



