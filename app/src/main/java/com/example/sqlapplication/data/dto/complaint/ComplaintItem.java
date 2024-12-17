package com.example.sqlapplication.data.dto.complaint;


import com.example.sqlapplication.data.model.ThingView;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ComplaintItem {
    private Integer id;

    private Integer userId;

    private ThingView thingView;

    private Integer type;

    private String reason;

    private Integer state;
}
