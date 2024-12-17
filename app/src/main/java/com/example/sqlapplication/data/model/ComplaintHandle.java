package com.example.sqlapplication.data.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ComplaintHandle {
    private Integer id;

    private Integer complaintId;

    private Integer managerId;

    private String handleResult;
}