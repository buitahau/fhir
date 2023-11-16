package com.owt.trackingworkingtime.service;

import com.owt.trackingworkingtime.dto.TrackingDto;
import com.owt.trackingworkingtime.dto.TrackingRequestDto;

import java.util.List;

public interface TrackingService {
    TrackingDto save(TrackingDto trackingDto);

    List<TrackingDto> find(TrackingRequestDto trackingRequestDto);
}
