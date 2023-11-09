package com.owt.trackingworkingtime.service;

import com.owt.trackingworkingtime.dto.TrackingDto;

import java.util.Date;
import java.util.List;

public interface TrackingService {
    TrackingDto save(TrackingDto trackingDto);

    List<TrackingDto> find(String tagId, Date date);
}
