package com.owt.trackingworkingtime.dto;

import com.owt.trackingworkingtime.model.Tracking;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class TrackingDto {
    private String tagId;
    private Date trackingTime;

    public Tracking toTracking() {
        return new Tracking(tagId, trackingTime);
    }

    public static TrackingDto from(Tracking tracking) {
        TrackingDto dto = new TrackingDto();
        dto.setTrackingTime(tracking.getTrackingTime());
        dto.setTagId(tracking.getTagId().trim());
        return dto;
    }
}
