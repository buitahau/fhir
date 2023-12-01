package com.owt.trackingworkingtime.service;

import com.owt.trackingworkingtime.dto.TrackingCombinationDto;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface TrackingCombinationService {

    void aggregateTrackings(String tagId, Date date);

    Map<String, List<TrackingCombinationDto>> findByTagIdsAndDate(List<String> tagIds, Date checkIn, Date checkOut);
}
