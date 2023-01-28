package com.example.springsecurity.services;

import com.example.springsecurity.entities.Role;
import com.example.springsecurity.entities.User;

import java.util.List;
import java.util.Optional;

/**
 * @author manhdt14
 * created in 1/5/2023 6:25 PM
 */
public interface UserService {
    User saveUser(User user);
    Role saveRole(Role role);
    void addRoleToUser(String name, String roleName);
    Optional<User> getUser(String name);
    List<User> getUsers();
}
