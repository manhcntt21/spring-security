package com.example.springsecurity.controllers;

import com.example.springsecurity.request.AuthRequest;
import com.example.springsecurity.config.JwtService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    private JwtService jwtService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @PostMapping()
    public String authenticateAndGetToken(@RequestBody AuthRequest authRequest) {
        log.info("authentication starting");
        Authentication authentication =  authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
        if(authentication.isAuthenticated()) {
            return jwtService.generateToken(authRequest.getUsername());
        } else {
            throw new UsernameNotFoundException("invalid user request!");
        }
    }
}
