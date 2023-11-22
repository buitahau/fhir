package com.owt.trackingworkingtime.service;

import com.owt.trackingworkingtime.dto.TrackingDto;
import com.owt.trackingworkingtime.dto.TrackingRequestDto;
import com.owt.trackingworkingtime.repository.TrackingRepository;
import com.owt.trackingworkingtime.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class TrackingServiceImpl implements TrackingService {

    @Autowired
    private TrackingRepository trackingRepository;

    @Override
    public TrackingDto save(TrackingDto trackingDto) {
        return TrackingDto.from(trackingRepository.save(trackingDto.toTracking()));
    }

    @Override
    public List<TrackingDto> find(TrackingRequestDto trackingRequestDto) {

        List<TrackingDto> allOfTrackingDto = new ArrayList<>();

        for (String tagId : trackingRequestDto.getTagIds()) {
            List<TrackingDto> trackingDtoByTagId =
                    trackingRepository.findByTagIdAndDate(tagId, trackingRequestDto.getCheckIn(), trackingRequestDto.getCheckOut())
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
    public List<String> findTagIdsByDatetime(Date date) {
        if (Objects.isNull(date)) date = new Date();
        return trackingRepository.findTagIdByDatetime(DateUtil.setZeroSecondAndMillisecond(date));
    }

    @Override
    public void deleteByDate(Date date) {
        trackingRepository.deleteByDate(date);
    }
}
