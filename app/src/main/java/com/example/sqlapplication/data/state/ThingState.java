package com.example.sqlapplication.data.state;

import lombok.Getter;

@Getter
public enum ThingState {
    UNSOLVED(0),  // 刚发布未处理
    DELETE(1),  // 被发布者删除了
    SOLVED(2);  // 已经处理了

    final int val;
    ThingState(int val) {
        this.val = val;
    }
}