package com.example.sqlapplication.data.dto.form;

import lombok.Getter;

@Getter
public class UpdateFormDto {
    private Integer id;

    /**
     * 必须是foundThing里面UNSOLVED的thingID
     */
    private Integer thingId;

}
