package com.example.sqlapplication.data.dto.complaint;

import com.example.sqlapplication.data.state.ComplaintType;
import com.google.firebase.database.annotations.NotNull;
import lombok.Getter;

@Getter
public class ComplaintDto {
    private Integer thingId;

    private ComplaintType type;

    private String reason;

}
