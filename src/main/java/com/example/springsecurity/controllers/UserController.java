package com.example.springsecurity.controllers;

import com.example.springsecurity.entities.Role;
import com.example.springsecurity.entities.User;
import com.example.springsecurity.request.RoleToUserRequest;
import com.example.springsecurity.services.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * @author manhdt14
 * created in 1/5/2023 5:24 PM
 */
@RestController
@RequestMapping("/api/v1")
@AllArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;


    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<User>> getUsers() {
        log.info("call getUsers");
        return ResponseEntity.ok().body(userService.getUsers());
    }

    @GetMapping("/user/{username}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<Optional<User>> getUser(@PathVariable("username") String name) {
        return ResponseEntity.ok().body(userService.getUser(name));
    }
    @GetMapping("/user/save")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<User> saveUser(@RequestBody User user) {
        return ResponseEntity.ok().body(userService.saveUser(user));
    }

    @GetMapping("/role/save")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Role> saveRole(@RequestBody Role role) {
        return ResponseEntity.ok().body(userService.saveRole(role));
    }

    @GetMapping("/role/addtouser")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> addRoleToUser(@RequestBody RoleToUserRequest form) {
        userService.addRoleToUser(form.getUsername(), form.getRoleName());
        return ResponseEntity.ok().build();
    }
}
