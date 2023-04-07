package com.example.springsecurity.controllers;

import com.example.springsecurity.request.AuthRequest;
import com.example.springsecurity.config.JwtService;
import com.example.springsecurity.response.AuthResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

/**
 * @author manhdt14
 * created in 1/27/2023 3:31 PM
 */
@RestController
@RequestMapping("/authenticate")

@Slf4j
public class AuthController {
    /**
     * initialized component was followed Spring IoC
     */
    private JwtService jwtService;

    /**
     * authentication configured in SecurityConfig.class
     */
    private AuthenticationManager authenticationManager;

    public AuthController(JwtService jwtService, AuthenticationManager authenticationManager) {
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping()
    public ResponseEntity<?> authenticateAndGetToken(@RequestBody AuthRequest authRequest) {
        log.info("authentication starting");
        Authentication authentication =  authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
        if(authentication.isAuthenticated()) {
            return ResponseEntity.ok(new AuthResponse(
                    jwtService.generateToken(authRequest.getUsername()),
                    jwtService.generateRefreshToken(authRequest.getUsername())));
        } else {
            throw new UsernameNotFoundException("invalid user request!");
        }
    }
}
