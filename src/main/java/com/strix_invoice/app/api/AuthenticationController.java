package com.strix_invoice.app.api;

import com.strix_invoice.app.Entity.Users;
import com.strix_invoice.app.exceptions.custom.UserAlreadyExistsException;
import com.strix_invoice.app.model.RegisterUserInfo;
import com.strix_invoice.app.model.UsersPrincipal;
import com.strix_invoice.app.records.UserRecord;
import com.strix_invoice.app.service.AuthenticationService;
import com.strix_invoice.app.service.JWTService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("${api.base-path}")
public class AuthenticationController {

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private JWTService jwtService;

    @PostMapping("/register")
    public ResponseEntity<Object> register(@RequestBody RegisterUserInfo user) {
        log.info("Request for User Registration by {} ", user.getName());
        Users registeredUser = authenticationService.findByEmail(user.getEmail());

        if (registeredUser != null) {
            log.info("User with email {} already registered with the user ID {}", user.getEmail(), registeredUser.getId());
            throw new UserAlreadyExistsException("email Already registered");
        }

        try {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            Users savedUser = authenticationService.register(user);

            log.info("User {} registered successfully with ID {}",user.getEmail(),savedUser.getId());
            return ResponseEntity.status(HttpStatus.CREATED).build();

        } catch (Error e) {
            log.error("User Registration failed for user {} ",user.getEmail());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
}

    @PostMapping("/login")
    public UserRecord login(@RequestBody Users users) {
        log.info("User {} requested for Authentication Token", users.getEmail());
        try {
            String token = authenticationService.authenticateUser(users);
            log.info("User {} Authenticated successfully and Auth Token Dispatched", users.getEmail());
            return new UserRecord(users.getEmail(), token);

        } catch (BadCredentialsException ex) {
            log.error("Authentication Failed for User {}", users.getEmail());
            throw ex;
        } catch (Exception ex) {
            log.error("An unexpected error occurred during authentication: {}", ex.getMessage(), ex);
            throw ex;
        }
    }

    @PostMapping("/authenticate")
    public ResponseEntity<?> authenticate(@RequestBody Users users, HttpServletResponse response) {
        System.out.println(users);
        log.info("User {} requested for Authentication Token ", users.getEmail());

        try {
            String token = authenticationService.authenticateUser(users);
            Cookie cookie = new Cookie("token", token);
            cookie.setHttpOnly(true); // Makes the cookie HTTP-only
            cookie.setSecure(false); // Ensure it's sent over HTTPS
            cookie.setPath("/");
            cookie.setMaxAge(1 * 60 * 60); // Cookie expires in 1 day
            response.addCookie(cookie);

            log.info("User {} Authenticated successfully and Auth Token Dispatched", users.getEmail());
            return ResponseEntity.ok("Authentication successful and Auth Token Dispatched");

        } catch (BadCredentialsException ex) {
            log.error("Authentication Failed for User {}", users.getEmail());
            throw ex;
        } catch (Exception ex) {
            log.error("An unexpected error occurred: {}", ex.getMessage(), ex);
            throw ex;
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
    public Users getUser(@AuthenticationPrincipal UsersPrincipal principal) {
        String userId = principal.getUsername();
        Users users = authenticationService.findByEmail(userId);
        return users;
    }


    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/users-info")
    public Users getUserInfo(@AuthenticationPrincipal UsersPrincipal principal) {
        String username = principal.getUsername();
        Users user = authenticationService.findByEmail(username);
        return user;
    }
}
