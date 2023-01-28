package com.example.springsecurity.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author manhdt14
 * created in 1/29/2023 2:22 AM
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleToUserRequest {
    private String username;
    private String roleName;
}
