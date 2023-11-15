package com.owt.trackingworkingtime.service;

import com.owt.trackingworkingtime.dto.TrackingCombinationDto;
import com.owt.trackingworkingtime.model.TrackingCombination;

import java.util.List;

public interface TrackingCombinationService {
    List<TrackingCombination> optimizeTrackingTime(TrackingCombinationDto trackingCombinationDto);

}
