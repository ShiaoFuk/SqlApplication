package com.example.sqlapplication.data.dto.thing;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

import java.util.Date;

@Getter
public class ThingDto {
    private String name;

    private String description;

    private String place;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")  // 指定日期格式
    private Date time;

}
