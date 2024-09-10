package com.strix_invoice.app.controller;

import com.strix_invoice.app.Entity.Users;
import com.strix_invoice.app.service.AuthenticationService;
import com.strix_invoice.app.service.JWTService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
public class AuthenticationControllers {

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private JWTService jwtService;

    @PostMapping("/register")
    public Users register(@RequestBody Users users) {
        System.out.println(users.getUsername() + " " + users.getPassword());
        users.setPassword(passwordEncoder.encode(users.getPassword()));
        return authenticationService.register(users);
    }

    @PostMapping("/login")
    public UserModel login(@RequestBody Users users) {
        System.out.println(users.getUsername() + " " + users.getPassword());
        String token = authenticationService.authenticateUser(users);
        Users user = authenticationService.getUserByUserName(users.getUsername());
        return new UserModel(user.getUsername(), user.getId(), "DVG", token);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<?> authenticate(@RequestBody Users users, HttpServletResponse response) {
        // Authenticate user and generate JWT token (details omitted)
        System.out.println(users.getUsername() + " " + users.getPassword());

        try {
            String token = authenticationService.authenticateUser(users);
            Cookie cookie = new Cookie("token", token);
            cookie.setHttpOnly(true); // Makes the cookie HTTP-only
            cookie.setSecure(false); // Ensure it's sent over HTTPS
            cookie.setPath("/");
            cookie.setMaxAge(1 * 60 * 60); // Cookie expires in 1 day
            response.addCookie(cookie);
            return ResponseEntity.ok("Login successful");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping("/logout-user")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        Cookie cookie = new Cookie("token", null);
        cookie.setHttpOnly(true);
        cookie.setSecure(false); // Use true if you're on HTTPS
        cookie.setPath("/");
        cookie.setMaxAge(0); // Expire the cookie
        response.addCookie(cookie);
        System.out.println("Logout Invoked" + cookie.getValue());
        return ResponseEntity.ok("Logout successful");
    }

    @PostMapping("/remove-cookie")
    public ResponseEntity<Void> removeCookie(HttpServletResponse response) {
        Cookie cookie = new Cookie("token", null);
        cookie.setHttpOnly(true);
        cookie.setSecure(false); // Use true if you're on HTTPS
        cookie.setPath("/");
        cookie.setMaxAge(0); // Expire the cookie
        response.addCookie(cookie);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/me")
    public UserModel getUser(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        Cookie jwtCookie = null;
        if (cookies != null) {
            jwtCookie = Arrays.stream(cookies)
                    .filter(cookie -> "token".equals(cookie.getName()))
                    .findFirst()
                    .orElse(null);
        }
        String username = null;
        String jwtToken = null;
        if (jwtCookie != null) {
            jwtToken = jwtCookie.getValue();
        }
        if (jwtCookie != null && jwtToken != null) {
            username = jwtService.extractUsername(jwtToken);
        }
        Users user = authenticationService.getUserByUserName(username);
        return new UserModel(user.getUsername(), user.getId(), "DVG", "Sample token");
    }

    @GetMapping("/users")
    public List<Users> getAllUsers() {
        return authenticationService.getAllUsers();
    }
}

record UserModel(String email, int userId, String city, String token) {
}