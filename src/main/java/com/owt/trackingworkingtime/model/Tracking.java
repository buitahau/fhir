package com.owt.trackingworkingtime.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;


@Entity
@Data
@IdClass(TrackingId.class)
@RequiredArgsConstructor
@NoArgsConstructor
@Table(name = "tracking", schema = "public")
public class Tracking {
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
}
