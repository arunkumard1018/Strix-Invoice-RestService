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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

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
        System.out.println(user);
        Users checkUser = authenticationService.findByEmail(user.getEmail());
        if (checkUser == null) {
            try {
                user.setPassword(passwordEncoder.encode(user.getPassword()));
                Users registerUser = authenticationService.register(user);
                return ResponseEntity.status(HttpStatus.CREATED).build();
            } catch (Error e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
            }
        }
        throw new UserAlreadyExistsException("email Already registered");
    }

    @PostMapping("/login")
    public UserRecord login(@RequestBody Users users) {
        String token = authenticationService.authenticateUser(users);
        Users user = authenticationService.findByEmail(users.getEmail());
        return new UserRecord(user.getEmail(), user.getId(), "DVG", token);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<?> authenticate(@RequestBody Users users, HttpServletResponse response) {
        log.info("User {} requested for Authentication ", users.getEmail());
        try {
            String token = authenticationService.authenticateUser(users);
            Cookie cookie = new Cookie("token", token);
            cookie.setHttpOnly(true); // Makes the cookie HTTP-only
            cookie.setSecure(false); // Ensure it's sent over HTTPS
            cookie.setPath("/");
            cookie.setMaxAge(1 * 60 * 60); // Cookie expires in 1 day
            response.addCookie(cookie);

            log.info("User {} Authenticated successfully", users.getEmail());
            return ResponseEntity.ok("Authentication successful");

        } catch (BadCredentialsException ex) {
            log.error("Exception : {}", ex.getMessage());
            log.error("Authentication Failed for User {}", users.getEmail());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Credentials");

        } catch (Exception ex) {
            log.error("An unexpected error occurred: {}", ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Authentication Failed");
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
    public UserRecord getUser(HttpServletRequest request) {
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
        Users user = authenticationService.findByEmail(username);
        return new UserRecord(user.getEmail(), user.getId(), "DVG", "Sample token");
    }

    @GetMapping("/users")
    public List<Users> getAllUsers() {
        return authenticationService.getAllUsers();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/users-info")
    public UserRecord getUserInfo(HttpServletRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Users user = authenticationService.findByEmail(username);
        Long userId = -1L;
        if (authentication != null && authentication.getPrincipal() instanceof UsersPrincipal) {
            UsersPrincipal userPrincipal = (UsersPrincipal) authentication.getPrincipal();
            userId = userPrincipal.getUserId();
            System.out.println("User's Id: " + userId);
        }
        return new UserRecord(user.getEmail(), userId, "DVG", "Sample token");
    }
}
