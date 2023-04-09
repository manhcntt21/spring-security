package com.example.springsecurity.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * @author manhdt14
 * created in 3/29/2023 11:59 PM
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@Order(1)
public class BasicAuthentication {
    /**
     * config security for api /authenticate
     * @param http
     * @return
     * @throws Exception
     */
    @Bean
    public SecurityFilterChain securityFilterChain2(HttpSecurity http) throws Exception{
        // enable cors, disable cors
        http
                .csrf().disable()
                .cors().and()
                .authorizeHttpRequests(
                        authorize -> authorize.requestMatchers("/greetings/**").permitAll()
                                .requestMatchers("/authenticate/**").authenticated()
                                .anyRequest().permitAll() // cho qua để còn chuyển sang cái security thứ 2
                )
                .httpBasic(Customizer.withDefaults());
        return http.build();
    }
}
