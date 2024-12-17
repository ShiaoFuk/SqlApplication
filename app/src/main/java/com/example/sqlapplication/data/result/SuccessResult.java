package com.example.sqlapplication.data.result;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class SuccessResult<T> extends Result<T> {
    public SuccessResult() {
        this.code = 200;
        this.message = "success";
    }

    public SuccessResult(T data) {
        this();
        this.data = data;
    }

    public SuccessResult(String message, T data) {
        this();
        this.message = message;
        this.data = data;
    }

}
