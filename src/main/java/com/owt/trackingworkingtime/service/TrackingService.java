package com.owt.trackingworkingtime.service;

import com.owt.trackingworkingtime.dto.TrackingDto;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

public interface TrackingService {
    @Cacheable(value = "tracking", key = "#trackingDto", sync = true)
    @Transactional
    TrackingDto save(TrackingDto trackingDto);

    List<String> findTagIdsByDate(Date date);

    List<String> findTagIdsByDatetime(Date date);
}
