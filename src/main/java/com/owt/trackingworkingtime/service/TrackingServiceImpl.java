package com.owt.trackingworkingtime.service;

import com.google.common.collect.Maps;
import com.owt.trackingworkingtime.dto.TrackingDto;
import com.owt.trackingworkingtime.dto.TrackingRequestDto;
import com.owt.trackingworkingtime.model.Tracking;
import com.owt.trackingworkingtime.model.TrackingId;
import com.owt.trackingworkingtime.repository.TrackingRepository;
import com.owt.trackingworkingtime.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TrackingServiceImpl implements TrackingService {

    @Autowired
    private TrackingRepository trackingRepository;

    @Override
    public TrackingDto save(TrackingDto trackingDto) {
        TrackingId trackingId = new TrackingId(trackingDto.getTagId(), trackingDto.getTrackingTime());

        Optional<Tracking> optionalTracking = trackingRepository.findById(trackingId);
        return optionalTracking.map(TrackingDto::from).orElseGet(() -> TrackingDto.from(trackingRepository.save(trackingDto.toTracking())));
    }

    @Override
    public List<TrackingDto> find(TrackingRequestDto trackingRequestDto) {
        Date fromDate = trackingRequestDto.getFromDate();
        String fromZone = DateUtil.getOnlyZone(fromDate);

        Date toDate = trackingRequestDto.getToDate();
        String toZone = DateUtil.getOnlyZone(toDate);

        List<TrackingDto> allOfTrackingDto = new ArrayList<>();

        for (String tagId : trackingRequestDto.getTagIds()) {
            List<TrackingDto> trackingDtoByTagId = trackingRepository.findByTagIdAndDate(tagId, fromDate, fromZone, toDate, toZone)
                    .stream().map(TrackingDto::from)
                    .toList();
            allOfTrackingDto.addAll(trackingDtoByTagId);
        }

        return allOfTrackingDto;
    }

    @Override
    public List<String> findTagIdsByDate(Date date) {
        return trackingRepository.findTagIdByDate(date);
    }

    @Override
    public void deleteByDate(Date date) {
        trackingRepository.deleteByDate(date);
    }
}
