package com.owt.trackingworkingtime.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@IdClass(TrackingCombinationId.class)
@Table(name = "tracking_combination", schema = "public")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TrackingCombination extends AbstractPersistableEntity<TrackingCombinationId> {
    @Id
    @Column(name = "tag_id")
    private String tagId;

    @Id
    @Column(name = "check_in")
    private Date checkIn;

    @Column(name = "check_out")
    private Date checkOut;

    @Override
    public TrackingCombinationId getId() {
        return new TrackingCombinationId(tagId, checkIn);
    }
}
