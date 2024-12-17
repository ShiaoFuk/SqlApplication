package com.example.sqlapplication.data.dto.thing;

import com.example.sqlapplication.data.dto.MyPageInfo;
import com.example.sqlapplication.data.state.ThingState;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public class ThingMyPageInfo extends MyPageInfo {
    ThingState thingState;

    public ThingMyPageInfo(Integer page, Integer pageSize, ThingState state) {
        super(page, pageSize);
        this.thingState = state;
    }
}
