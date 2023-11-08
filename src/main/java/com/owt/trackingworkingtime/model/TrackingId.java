package com.owt.trackingworkingtime.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@AllArgsConstructor
@EqualsAndHashCode
@NoArgsConstructor
@Data
public class TrackingId implements Serializable {
    private String tagId;
    private Date trackingTime;
}
