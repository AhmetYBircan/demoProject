package com.ayb.demo.controllers;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ayb.demo.services.JwtService;
import com.ayb.demo.services.LogService;
import com.ayb.demo.models.*;
import com.ayb.demo.services.UserRepository;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;



@RestController
@Controller
@RequestMapping("/user") 

public class UserController {

    @Autowired
    private UserRepository userRepo;

    @Autowired 
    private JwtService JwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private LogService logService;


    @GetMapping({"", "/"})
    public ResponseEntity<Map<String, Object>> showUserList() {
    List<User> users = userRepo.findAll(Sort.by(Sort.Direction.DESC, "id"));

    Map<String, Object> response = new HashMap<String, Object>();
    response.put("message", "İŞLEM BAŞARILI, TÜM USERLAR:");
    response.put("users", users);

    return new ResponseEntity<>(response, HttpStatus.OK);
}


    @PostMapping("/register")
    public ResponseEntity<Object> registerUser ( @Valid @RequestBody RegisterDto request) {

        var bCryptEncoder = new BCryptPasswordEncoder();


        User user =  new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setCompany(request.getCompany());
        user.setCountry(request.getCountry());
        user.setCreatedAt(new Date());

        user.setPassword(bCryptEncoder.encode(request.getPassword()));

        try {
            var mailExistCheck = userRepo.findByEmail(request.getEmail());
            if (mailExistCheck != null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email already exists");
            }
            userRepo.save(user);
            String token = JwtService.createJwtToken(user);

            var response = new HashMap<String, Object>();
            response.put("message", "User registered successfully");
            response.put("Access Token", token);
            response.put("User", user);

            logService.log("User registed", "REGISTER", user.getEmail(),null,null);


            return ResponseEntity.ok(response);
            

        } catch (Exception e) {
            System.out.println("Exception Of Register: " + e.getMessage());
            logService.log(e.getMessage() , "REGISTER FAIL", user.getEmail(),null,null);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred during registration");
        }


    }


    @PostMapping("/login")
    public ResponseEntity<Object> login (
        @Valid @RequestBody LoginDto request
    ) {
        try {   
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );

            User user =  userRepo.findByEmail((request.getEmail()));

            String accessToken =  JwtService.createJwtToken(user);

            var loginResponse = new HashMap<String,Object> ();
            loginResponse.put("accessToken", accessToken);
            loginResponse.put("user", user);

            logService.log("User logged in", "LOGIN", user.getEmail(),null,null);

            return ResponseEntity.ok(loginResponse);
        }catch (Exception e) {
            logService.log(e.getMessage() , "LOGIN FAIL", request.getEmail(),null,null);
            System.out.println("Exception Of Login: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred during login");
        }
    }


    @GetMapping("/me")
    public ResponseEntity<Object> userMe(Authentication auth) {
        var response = new HashMap  <String, Object> ();

        response.put("Username", auth.getName());
        response.put("Authorities", auth.getAuthorities());

        var user = userRepo.findByEmail(auth.getName());
        response.put("User", user);
        

        return ResponseEntity.ok(response);
    }
    @GetMapping("byId/{id}") 
    public ResponseEntity <?> getUserById (
    @PathVariable int id,
        Model model
    ) {
        try{
            User user = userRepo.findById(id).get();
            return ResponseEntity.ok(user);
        }
        catch(Exception e) {
            System.out.println("Exception Of Get by Id:" + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ürün bulunamadı.");
        }
        
    }

    

    @PatchMapping("/edit/{id}")
    public ResponseEntity<Object> updateUsertById(
        Authentication auth,
        @PathVariable int id, @RequestBody UserDto request) {
        try {

            User updateUser = userRepo.findById(id).get();
            if (!userRepo.existsById(id)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
            }
            if (!updateUser.getEmail().equals(auth.getName())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You are not authorized to update this user");
            }
            
        if (request.getName() != null) {
            updateUser.setName(request.getName());
        }
        if (request.getEmail() != null) {
            updateUser.setEmail((request.getEmail()));
        }
        if (request.getCompany() != null) {
            updateUser.setCompany(request.getCompany());
        }

        if (request.getCountry() != null) {
            updateUser.setCountry(request.getCountry());
        }
        logService.log("User edited", "PATCH USER", auth.getName(),null,null);
        
            userRepo.save(updateUser); 
            return ResponseEntity.ok(updateUser);
           
            
        }catch(Exception e) {
            logService.log(e.getMessage() , "PATCH USER FAIL", auth.getName(),null,null);
            System.out.println("Exception Of Update: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred during update");
        }
    }


    @DeleteMapping("/delete/{id}")
    private ResponseEntity<String> deleteUsersById(
        Authentication auth,
        @PathVariable int id) {
        try {
            if (!userRepo.existsById(id)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
            }
            User user = userRepo.findById(id).get();
            if (!user.getEmail().equals(auth.getName())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You are not authorized to delete this user");
            }

            logService.log("User deleted", "DELETE USER", auth.getName(),null,null);

            userRepo.deleteById(id);

            return ResponseEntity.status(HttpStatus.OK).body("Deletion successful");
        }catch (Exception e) {

            logService.log(e.getMessage() , "DELETE USER FAIL", auth.getName(),null,null);

            System.out.println("Exception Of Delete: " + e.getMessage());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred during deletion");
        }
    }
 
}
