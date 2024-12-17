package com.example.sqlapplication.data.state;

import lombok.Getter;

/**
 * 对物品投诉的类型
 */
@Getter
public enum ComplaintType {
    OTHER(0),  // 其他原因
    ERROR_TAKE(1),  // 错误领回
    IRRELEVANT(2),  // 无关内容
    FAKE(3),  // 虚假信息
    ;


    final int val;
    ComplaintType(int val) {
        this.val = val;
    }
}
