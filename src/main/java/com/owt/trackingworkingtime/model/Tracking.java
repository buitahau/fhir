package com.owt.trackingworkingtime.model;

import com.owt.trackingworkingtime.util.DateUtil;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.util.Date;

@Entity
@IdClass(TrackingId.class)
@NoArgsConstructor
@Table(name = "tracking", schema = "public")
@Getter
public class Tracking implements Serializable {

    @Id
    @NonNull
    @Column(name = "tag_id")
    private String tagId;

    @Id
    @NonNull
    @Column(name = "tracking_time")
    private Date trackingTime;

    @CreationTimestamp
    @Column(name = "created_date")
    private Date createdDate;

    @UpdateTimestamp
    @Column(name = "modified_date")
    private Date modifiedDate;

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
}
