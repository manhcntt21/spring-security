package com.example.springsecurity.config;

import com.example.springsecurity.entities.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author manhdt14
 * created in 1/27/2023 9:45 PM
 */
public class CustomUserDetails implements UserDetails {
    private String name;
    private String password;
    private List<GrantedAuthority> authorities = new ArrayList<>();
    public CustomUserDetails(User user) {
        name = user.getName();
        password = user.getPassword();
        user.getRoles().forEach(r -> authorities.add(new SimpleGrantedAuthority(r.getName())));
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return name;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
