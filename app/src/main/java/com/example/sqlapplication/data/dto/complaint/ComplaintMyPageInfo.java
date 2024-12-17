package com.example.sqlapplication.data.dto.complaint;

import com.example.sqlapplication.data.dto.MyPageInfo;
import com.example.sqlapplication.data.state.ComplaintState;

import lombok.Getter;

@Getter
public class ComplaintMyPageInfo extends MyPageInfo {
    ComplaintState complaintState;

    public ComplaintMyPageInfo(Integer page, Integer pageSize, ComplaintState complaintState) {
        super(page, pageSize);
        this.complaintState = complaintState;
    }
}
