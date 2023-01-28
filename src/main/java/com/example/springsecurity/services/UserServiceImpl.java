package com.example.springsecurity.services;

import com.example.springsecurity.entities.Role;
import com.example.springsecurity.entities.User;
import com.example.springsecurity.repositories.RoleRepository;
import com.example.springsecurity.repositories.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * @author manhdt14
 * created in 1/27/2023 5:10 AM
 */
@Service
@AllArgsConstructor
@Transactional
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        User user = userRepository.findByUsername(username);
//        if (user == null) {
//            log.error("user not found in the database");
//            throw new UsernameNotFoundException("user not found in the database");
//        } else {
//            log.error("user found in the database: {}", username);
//        }
//        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
//        user.getRoles().forEach(role -> authorities.add(new SimpleGrantedAuthority(role.getName())));
//        return new org.springframework.security.core.userdetails.User(
//                user.getUsername(),
//                user.getPassword(),
//                authorities
//        );

//    }
    @Override
    public User saveUser(User user) {
        log.info("saving new user {} to the database", user.getName());
        return userRepository.save(user);
    }

    @Override
    public Role saveRole(Role role) {
        log.info("saving new role {} to the database", role.getName());
        return roleRepository.save(role);
    }

    @Override
    public void addRoleToUser(String name, String roleName) {
        log.info("add new role {} to user {}", roleName, name);
        Optional<User> user = userRepository.findByName(name);
        Role role = roleRepository.findByName(roleName);
        user.get().getRoles().add(role);
    }

    @Override
    public Optional<User> getUser(String name) {
        log.info("fetching user {}", name);
        return userRepository.findByName(name);
    }

    @Override
    public List<User> getUsers() {
        log.info("fetching users");
        return userRepository.findAll();
    }
}
