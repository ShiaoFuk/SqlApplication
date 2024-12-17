package com.example.sqlapplication.data.dto.form;

import com.example.sqlapplication.data.state.FormState;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FormDto {
    private Integer thingId;

    private FormState state;
}
