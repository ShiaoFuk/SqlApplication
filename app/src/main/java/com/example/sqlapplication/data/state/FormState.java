package com.example.sqlapplication.data.state;

import lombok.Getter;

@Getter
public enum FormState {
    UNSOLVED(0),
    DELETE(1),
    SOLVED(2);


    final int val;

    FormState(int val) {
        this.val = val;
    }

    public static FormState getState(int val) {
        switch (val) {
            case 0:
                return UNSOLVED;
            case 1:
                return DELETE;
            case 2:
                return SOLVED;
            default:
        }
        return UNSOLVED;
    }
}
