package com.example.springsecurity.controllers;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * @author manhdt14
 * created in 1/6/2023 11:08 PM
 */
@RestController
@RequestMapping("/cookie")
public class CookieController {
    @GetMapping
    public String getCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        return Arrays.stream(cookies)
                .map(c -> c.getValue())
                .collect(Collectors.joining(", "));
    }
}
