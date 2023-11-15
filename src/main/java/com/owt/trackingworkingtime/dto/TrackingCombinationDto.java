package com.owt.trackingworkingtime.dto;

import com.owt.trackingworkingtime.model.TrackingCombination;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Getter
@Setter
public class TrackingCombinationDto {
    private String tagId;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Date checkIn;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Date checkOut;

    public TrackingCombination toTrackingCombination() {
        return new TrackingCombination(tagId, checkIn, checkOut);
    }

    public static TrackingCombinationDto from(TrackingCombination trackingCombination) {
        TrackingCombinationDto dto = new TrackingCombinationDto();
        dto.setTagId(trackingCombination.getTagId().trim());
        dto.setCheckIn(trackingCombination.getCheckIn());
        dto.setCheckOut(trackingCombination.getCheckOut());
        return dto;
    }
}
