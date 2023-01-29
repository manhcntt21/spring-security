package com.example.springsecurity.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author manhdt14
 * created in 1/27/2023 3:35 PM
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthRequest {
    String username;
    String password;
}
