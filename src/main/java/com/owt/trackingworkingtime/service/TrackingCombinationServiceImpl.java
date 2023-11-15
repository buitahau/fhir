package com.owt.trackingworkingtime.service;

import com.owt.trackingworkingtime.constant.TrackingConstant;
import com.owt.trackingworkingtime.dto.TrackingCombinationDto;
import com.owt.trackingworkingtime.model.Tracking;
import com.owt.trackingworkingtime.model.TrackingCombination;
import com.owt.trackingworkingtime.repository.TrackingCombinationRepository;
import com.owt.trackingworkingtime.repository.TrackingRepository;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class TrackingCombinationServiceImpl implements TrackingCombinationService {

    @Autowired
    TrackingRepository trackingRepository;

    @Autowired
    TrackingCombinationRepository trackingCombinationRepository;

    @Override
    public List<TrackingCombination> optimizeTrackingTime(TrackingCombinationDto trackingCombinationDto) {
        String tagId = trackingCombinationDto.getTagId();
        List<Tracking> listTracking = trackingRepository.findByTagIdAndDate(
                tagId, trackingCombinationDto.getCheckIn(), trackingCombinationDto.getCheckOut());

        if (CollectionUtils.isEmpty(listTracking)) {
            return new ArrayList<>();
        }

        Date checkIn = listTracking.get(0).getTrackingTime();
        Date checkOut = DateUtils.addMinutes(checkIn, TrackingConstant.TIMEOUT_BY_MINUTE);
        List<TrackingCombination> listTrackingCombination = new ArrayList<>();

        for (int i = 1; i < listTracking.size(); i++) {
            Tracking tracking = listTracking.get(i);
            Date currentTrackingTime = tracking.getTrackingTime();

            if (DateUtils.isSameInstant(checkOut, currentTrackingTime)) {
                checkOut = DateUtils.addMinutes(currentTrackingTime, TrackingConstant.TIMEOUT_BY_MINUTE);
                continue;
            }

            //Add data
            TrackingCombination trackingCombination = new TrackingCombination(tagId, checkIn, checkOut);
            listTrackingCombination.add(trackingCombination);
            checkIn = currentTrackingTime;
            checkOut = DateUtils.addMinutes(checkIn, TrackingConstant.TIMEOUT_BY_MINUTE);
        }

        //Add last data
        TrackingCombination trackingCombination = new TrackingCombination(tagId, checkIn, checkOut);
        listTrackingCombination.add(trackingCombination);

        return trackingCombinationRepository.saveAll(listTrackingCombination);
    }

    //To do
    public List<TrackingCombination> mergeTrackingCombinationData(
            List<TrackingCombination> draftData, String tagId, Date timeTracking) {
        List<TrackingCombination> existingData = trackingCombinationRepository.findByTagIdAndDate(tagId, timeTracking);

        if (CollectionUtils.isEmpty(existingData)){
            return trackingCombinationRepository.saveAll(draftData);
        }


        return null;
    }
}
