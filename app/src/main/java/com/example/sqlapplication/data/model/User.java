package com.example.sqlapplication.data.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private Integer id;

    private String certificateNumber;

    private String username;

    private String email;

    private String password;

    private Integer permission;

    private Integer state;
}