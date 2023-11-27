package com.owt.trackingworkingtime.service;

import com.owt.trackingworkingtime.dto.TrackingDto;
import com.owt.trackingworkingtime.repository.TrackingRepository;
import com.owt.trackingworkingtime.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class TrackingServiceImpl implements TrackingService {

    @Autowired
    private TrackingRepository trackingRepository;

    @Override
    public TrackingDto save(TrackingDto trackingDto) {
        return TrackingDto.from(trackingRepository.save(trackingDto.toTracking()));
    }

    @Override
    public List<String> findTagIdsByDate(Date date) {
        return trackingRepository.findTagIdsByDate(date);
    }

    @Override
    public List<String> findTagIdsByDatetime(Date date) {
        return trackingRepository.findTagIdsByDatetime(DateUtil.setZeroSecondAndMillisecond(date));
    }
}
