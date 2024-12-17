package com.example.sqlapplication.data.result;

import lombok.Data;

@Data
public abstract class Result<T> {
    protected Integer code;
    protected String message;
    protected T data;
}
