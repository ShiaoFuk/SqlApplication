package com.example.sqlapplication.data.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FoundThing {
    private Integer id;

    private Integer thingId;

    private Integer state;

    private Integer userId;
}