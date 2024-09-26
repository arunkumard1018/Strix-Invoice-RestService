/**
 * The {@code JWTFilter$} class represents Functionalities
 *
 * @author ArunKumar D
 */

package com.strix_invoice.app.filters;

import com.strix_invoice.app.service.CustomUserDetailsService;
import com.strix_invoice.app.service.JWTService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

@Slf4j
@Component
public class JWTFilter extends OncePerRequestFilter {

    @Autowired
    private JWTService jwtService;

    @Autowired
    private ApplicationContext context;

    @Value("${api.base-path}")
    private String basePath;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String authorizationHeader = request.getHeader("Authorization");

        String ipAddress = request.getRemoteAddr();
        // Get User-Agent header (information about client browser or device)
        String userAgent = request.getHeader("User-Agent");
        log.info("Request received from IP: {} with User-Agent: {}", ipAddress, userAgent);

        /* Allowed Public Paths */
        String path = request.getRequestURI();
        if (path.startsWith(basePath + "/register") ||
                path.startsWith(basePath + "/login") ||
                path.startsWith(basePath + "/authenticate") ||
                path.startsWith(basePath + "/app") ||
                path.startsWith(basePath + "/remove-cookie")) {
            filterChain.doFilter(request, response);
            return;
        }

        String username = null;
        String jwtToken = null;

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwtToken = authorizationHeader.substring(7);
            log.info("JWT Token found in Authorization header Proceeding for Authentication.");

        } else {
            // Check cookies for JWT token
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                Cookie jwtCookie = Arrays.stream(cookies)
                        .filter(cookie -> "token".equals(cookie.getName()))
                        .findFirst()
                        .orElse(null);

                if (jwtCookie != null) {
                    jwtToken = jwtCookie.getValue();
                    log.info("JWT Token found in cookie Proceeding for Authentication.");
                }
            }
        }

        try {
            if (jwtToken != null) {
                username = jwtService.extractUsername(jwtToken);

                log.info("Extracted username from JWT Token: {}", username);
            } else {
                log.warn("Accessss Denied: No JWT Token provided.");
                Cookie cookie = new Cookie("token", null);
                cookie.setHttpOnly(true);
                cookie.setSecure(false);  // Set true if you're on HTTPS
                cookie.setPath("/");
                cookie.setMaxAge(0);  // Expire the cookie
                response.addCookie(cookie);

                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Invalid JWT Token");
//                return;
            }

        } catch (Exception e) {
            log.error("Access Denied : {}", e.getMessage());

            // Expire the JWT cookie if token is invalid
            Cookie cookie = new Cookie("token", null);
            cookie.setHttpOnly(true);
            cookie.setSecure(false);  // Set true if you're on HTTPS
            cookie.setPath("/");
            cookie.setMaxAge(0);  // Expire the cookie
            response.addCookie(cookie);

            log.info("JWT Token has been cleared from the cookie.");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("JWT Token is expired or Invalid");
            return;
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            log.info("Attempting to authenticate user: {}", username);

            UserDetails userDetails = context.getBean(CustomUserDetailsService.class).loadUserByUsername(username);

            if (jwtService.validateToken(jwtToken, userDetails)) {
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
                log.info("User {} authenticated successfully", username);
            } else {
                log.warn("JWT Token validation failed for user: {}", username);
            }
        }

        filterChain.doFilter(request, response);
    }
}
