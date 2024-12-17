package com.example.sqlapplication.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
public class MyPageInfo {
    Integer page;
    Integer pageSize;
}
