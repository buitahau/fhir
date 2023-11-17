package com.owt.trackingworkingtime.service;

import com.owt.trackingworkingtime.dto.TrackingDto;
import com.owt.trackingworkingtime.dto.TrackingRequestDto;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Transactional
public interface TrackingService {
    TrackingDto save(TrackingDto trackingDto);

    List<TrackingDto> find(TrackingRequestDto trackingRequestDto);

    List<String> findTagIdsByDate(Date date);

    void deleteByDate(Date date);
}
