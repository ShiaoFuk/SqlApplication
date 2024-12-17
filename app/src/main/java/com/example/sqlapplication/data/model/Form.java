package com.example.sqlapplication.data.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Form {
    private Integer id;

    private Integer thingId;

    private Integer userId;

    private Integer state;
}