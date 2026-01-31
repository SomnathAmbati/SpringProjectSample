package com.example.SpringProject.user;

import com.example.SpringProject.common.AppEnums;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserDTO {
// private String name;
    private String email;
    private String password;

    // private AppEnums.RoleType role;
}