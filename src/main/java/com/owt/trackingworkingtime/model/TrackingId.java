package com.owt.trackingworkingtime.model;

import com.owt.trackingworkingtime.util.DateUtil;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

@EqualsAndHashCode
@Getter
@Setter
public class TrackingId implements Serializable {

    private String tagId;

    private Date trackingTime;

    public TrackingId() {
    }

    public TrackingId(String tagId, Date trackingTime) {
        this.tagId = tagId;
        setTrackingTime(trackingTime);
    }

    public void setTrackingTime(Date trackingTime) {
        this.trackingTime = DateUtil.setZeroSecondAndMillisecond(trackingTime);
    }
}
