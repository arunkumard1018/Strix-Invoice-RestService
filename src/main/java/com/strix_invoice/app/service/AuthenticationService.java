package com.strix_invoice.app.service;

import com.strix_invoice.app.Entity.Users;
import com.strix_invoice.app.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthenticationService {
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private JWTService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    public Users register(Users user){
        return usersRepository.save(user);
    }

    public String authenticateUser(Users users){
        Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(users.getUsername(), users.getPassword()));

        if(authenticate.isAuthenticated()){
            return jwtService.generateToken(users.getUsername());
        }else{
            return "Authentication FAILED";
        }
    }


    public List<Users> getAllUsers() {
        return usersRepository.findAll();
    }
}
