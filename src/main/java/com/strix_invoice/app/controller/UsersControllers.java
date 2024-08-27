package com.strix_invoice.app.controller;

import com.strix_invoice.app.Entity.Users;
import com.strix_invoice.app.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UsersControllers {

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public Users register(@RequestBody Users users){
        users.setPassword(passwordEncoder.encode(users.getPassword()));
        return authenticationService.register(users);
    }

    @PostMapping("/login")
    public String login(@RequestBody Users users){
        return authenticationService.authenticateUser(users);
    }



    @GetMapping("/users")
    public List<Users> getAllUsers(){
        return authenticationService.getAllUsers();
    }

}
