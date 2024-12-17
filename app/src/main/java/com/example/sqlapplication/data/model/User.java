package com.example.sqlapplication.data.model;

import com.example.sqlapplication.data.state.UserPermission;

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

    public UserPermission resPermission() {
        if (permission == 0) {
            return UserPermission.NORMAL;
        }
        if (permission == 1) {
            return UserPermission.MANAGER;
        }
        if (permission == 2) {
            return UserPermission.ROOT;
        }
        return UserPermission.NORMAL;
    }

}