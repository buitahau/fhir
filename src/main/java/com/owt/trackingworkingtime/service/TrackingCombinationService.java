package com.owt.trackingworkingtime.service;

import com.owt.trackingworkingtime.dto.TrackingRequestDto;
import com.owt.trackingworkingtime.dto.TrackingResponseDto;

import java.util.Date;
import java.util.List;

public interface TrackingCombinationService {

    void aggregateTrackings(String tagId, Date date);

    List<TrackingResponseDto> findByTagIdsAndDate(TrackingRequestDto trackingRequestDto);
}
