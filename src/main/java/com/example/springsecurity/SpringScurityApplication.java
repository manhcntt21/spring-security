package com.example.springsecurity;

import com.example.springsecurity.entities.Role;
import com.example.springsecurity.entities.User;
import com.example.springsecurity.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;

@SpringBootApplication
public class SpringScurityApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringScurityApplication.class, args);
    }

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Bean
    CommandLineRunner run(UserService userService) {
        return args -> {
            userService.saveRole(new Role(null, "ROLE_USER"));
            userService.saveRole(new Role(null, "ROLE_MANAGER"));
            userService.saveRole(new Role(null, "ROLE_ADMIN"));
            userService.saveRole(new Role(null, "ROLE_SUPER_ADMIN"));

            userService.saveUser(new User(null,  "manh", "manh@gmail.com", passwordEncoder.encode("1234"), new ArrayList<>()));
            userService.saveUser(new User(null,  "minh", "minh@gmail.com",passwordEncoder.encode("1234"), new ArrayList<>()));
            userService.saveUser(new User(null,  "ha", "ha@gmail.com",passwordEncoder.encode("1234"), new ArrayList<>()));
            userService.saveUser(new User(null, "thuy", "thuy@gmail.com", passwordEncoder.encode("1234"), new ArrayList<>()));

            userService.addRoleToUser("manh", "ROLE_USER");
//            userService.addRoleToUser("manh", "ROLE_MANAGER");
//            userService.addRoleToUser("manh", "ROLE_ADMIN");
//            userService.addRoleToUser("minh", "ROLE_MANAGER");
            userService.addRoleToUser("ha", "ROLE_ADMIN");
//            userService.addRoleToUser("thuy", "ROLE_SUPER_ADMIN");

        };
    }
}
