package com.ayb.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {


    @Autowired
    private UserRepository userRepo;


    @Override
    public UserDetails loadUserByUsername (String email) throws UsernameNotFoundException {
        var user = userRepo.findByEmail(email);

       if  (user != null) {

             UserDetails appUser = User.withUsername(user.getEmail())
                .password(user.getPassword())
                .build();


            return appUser;
       }

       return null;
    }
}
