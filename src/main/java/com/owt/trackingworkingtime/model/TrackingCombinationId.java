package com.owt.trackingworkingtime.model;

import lombok.*;

import java.io.Serializable;
import java.util.Date;

@EqualsAndHashCode
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TrackingCombinationId implements Serializable {
    private String tagId;
    private Date checkIn;
}
