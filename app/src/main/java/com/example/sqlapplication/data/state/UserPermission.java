package com.example.sqlapplication.data.state;

import lombok.Getter;

@Getter
public enum UserPermission {
    NORMAL(0),
    MANAGER(1),  // 可以审批form
    ROOT(2);  // 可以修改用户权限

    final int val;
    UserPermission(int val) {
        this.val = val;
    }
}
