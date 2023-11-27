package com.owt.trackingworkingtime.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.owt.trackingworkingtime.model.TrackingCombination;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class TrackingCombinationDto {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String tagId;
    private Date checkIn;
    private Date checkOut;

    public static TrackingCombinationDto from(TrackingCombination trackingCombination) {
        TrackingCombinationDto trackingCombinationDto = new TrackingCombinationDto();
        trackingCombinationDto.setCheckIn(trackingCombination.getCheckIn());
        trackingCombinationDto.setCheckOut(trackingCombination.getCheckOut());
        return trackingCombinationDto;
    }
}
