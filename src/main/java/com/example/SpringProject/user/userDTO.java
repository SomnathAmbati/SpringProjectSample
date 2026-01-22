package com.example.SpringProject.user;

import com.example.SpringProject.common.AppEnums;

import lombok.Data;

@Data
public class userDTO {
private String name;
    private String email;
    private String password;

    private AppEnums.RoleType role;
}