package com.owt.trackingworkingtime.dto;

import com.owt.trackingworkingtime.model.TrackingCombination;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TimeSheet {
    private Date checkIn;
    private Date checkOut;

    public TimeSheet from(TrackingCombination trackingCombination) {
        checkIn = trackingCombination.getCheckIn();
        checkOut = trackingCombination.getCheckOut();
        return this;
    }
}
