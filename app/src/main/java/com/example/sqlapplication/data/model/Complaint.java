package com.example.sqlapplication.data.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Complaint {
    private Integer id;

    private Integer userId;

    private Integer thingId;

    private Integer type;

    private String reason;

    private Integer state;
}