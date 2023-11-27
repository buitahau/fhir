package com.owt.trackingworkingtime.model;

import com.owt.trackingworkingtime.util.DateUtil;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Entity
@IdClass(TrackingId.class)
@NoArgsConstructor
@Table(name = "tracking", schema = "public")
@Getter
public class Tracking extends AbstractPersistableEntity<TrackingId> {

    @Id
    @Column(name = "tag_id")
    private String tagId;

    @Id
    @Column(name = "tracking_time")
    private Date trackingTime;

    public Tracking(String tagId, Date trackingTime) {
        this.tagId = tagId;
        setTrackingTime(trackingTime);
    }

    public void setTagId(String tagId) {
        this.tagId = tagId;
    }

    public void setTrackingTime(Date trackingTime) {
        this.trackingTime = DateUtil.setZeroSecondAndMillisecond(trackingTime);
    }

    @Override
    public TrackingId getId() {
        return new TrackingId(tagId, trackingTime);
    }
}