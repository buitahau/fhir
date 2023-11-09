package com.owt.trackingworkingtime.service;

import com.owt.trackingworkingtime.dto.TrackingDto;
import com.owt.trackingworkingtime.model.Tracking;
import com.owt.trackingworkingtime.model.TrackingId;
import com.owt.trackingworkingtime.repository.TrackingRepository;
import com.owt.trackingworkingtime.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class TrackingServiceImpl implements TrackingService {

    @Autowired
    private TrackingRepository trackingRepository;

    @Override
    public TrackingDto save(TrackingDto trackingDto) {
        TrackingId trackingId = new TrackingId(trackingDto.getTagId(), trackingDto.getTrackingTime());

        Optional<Tracking> optionalTracking = trackingRepository.findById(trackingId);
        if (optionalTracking.isPresent()) {
            return TrackingDto.from(optionalTracking.get());
        }

        return TrackingDto.from(trackingRepository.save(trackingDto.toTracking()));
    }

    @Override
    public List<TrackingDto> find(String tagId, Date date) {
        String zone = DateUtil.getOnlyZone(date);

        return trackingRepository.findByTagIdAndDate(tagId, date, zone)
                .stream().map(TrackingDto::from)
                .collect(Collectors.toList());
    }
}
