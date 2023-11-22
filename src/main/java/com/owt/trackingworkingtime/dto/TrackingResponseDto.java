package com.owt.trackingworkingtime.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class TrackingResponseDto {
    private String tagId;
    private List<TimeSheet> timeSheets;
}
