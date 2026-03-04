package com.example.demo.io;

import lombok.Data;

@Data
public class LoginRequest {
    private String userID;
    private String password;
}