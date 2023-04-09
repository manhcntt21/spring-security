package com.example.springsecurity.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author manhdt14
 * created in 4/9/2023 2:35 PM
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthRefresh {
    String username;
    String refresh;
}
