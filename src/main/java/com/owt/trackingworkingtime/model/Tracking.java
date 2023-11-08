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
@RequiredArgsConstructor
@NoArgsConstructor
@Table(name = "tracking", schema = "public")
@Getter
@Setter
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
    @Column(name = "created_on")
    private Date createdOn;

    @UpdateTimestamp
    @Column(name = "updated_on")
    private Date updatedOn;

    public void setTrackingTime(Date trackingTime) {
        this.trackingTime = DateUtil.setZeroSecondAndMillisecond(trackingTime);
    }
}
