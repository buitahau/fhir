package com.owt.trackingworkingtime.service;

import com.owt.trackingworkingtime.constant.TrackingConstant;
import com.owt.trackingworkingtime.dto.TrackingCombinationDto;
import com.owt.trackingworkingtime.model.Tracking;
import com.owt.trackingworkingtime.model.TrackingCombination;
import com.owt.trackingworkingtime.repository.TrackingCombinationRepository;
import com.owt.trackingworkingtime.repository.TrackingRepository;
import com.owt.trackingworkingtime.util.DateUtil;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class TrackingCombinationServiceImpl implements TrackingCombinationService {

    @Autowired
    private TrackingRepository trackingRepository;

    @Autowired
    private TrackingCombinationRepository trackingCombinationRepository;

    @Override
    public void aggregateTrackings(String tagId, Date date) {
        List<Tracking> trackings = trackingRepository.findByTagIdAndDate(tagId, date);

        if (CollectionUtils.isEmpty(trackings)) {
            return;
        }

        List<TrackingCombination> draftTrackingCombinations = calculateTrackingCombination(tagId, trackings);

        List<TrackingCombination> existingTrackingCombinations =
                trackingCombinationRepository.findByTagIdAndDate(tagId, date);

        mergeTrackingCombination(draftTrackingCombinations, existingTrackingCombinations, tagId, date);

        trackingRepository.deleteAll(trackings);
    }

    @Override
    public Map<String, List<TrackingCombinationDto>> findByTagIdsAndDate(List<String> tagIds, Date checkIn, Date checkOut) {
        List<TrackingCombination> trackingCombinations = trackingCombinationRepository.findByTagIdsAndDate(
                tagIds,
                DateUtil.setZeroSecondAndMillisecond(checkIn),
                DateUtil.setZeroSecondAndMillisecond(checkOut)
        );

        return trackingCombinations.stream().collect(Collectors.groupingBy(
                TrackingCombination::getTagId, Collectors.mapping(TrackingCombinationDto::from, Collectors.toList())));
    }

    private List<TrackingCombination> calculateTrackingCombination(String tagId, List<Tracking> trackings) {

        Date checkIn = trackings.get(0).getTrackingTime();
        Date checkOut = DateUtils.addMinutes(checkIn, TrackingConstant.TIMEOUT_BY_MINUTE);

        List<TrackingCombination> trackingCombinations = new ArrayList<>();

        for (int i = 1; i < trackings.size(); i++) {
            Tracking tracking = trackings.get(i);
            Date currentTrackingTime = tracking.getTrackingTime();

            if (DateUtils.isSameInstant(checkOut, currentTrackingTime)) {
                checkOut = DateUtils.addMinutes(currentTrackingTime, TrackingConstant.TIMEOUT_BY_MINUTE);
                continue;
            }

            TrackingCombination trackingCombination = new TrackingCombination(tagId, checkIn, checkOut);
            trackingCombinations.add(trackingCombination);

            checkIn = currentTrackingTime;
            checkOut = DateUtils.addMinutes(checkIn, TrackingConstant.TIMEOUT_BY_MINUTE);
        }

        //Add last data
        TrackingCombination trackingCombination = new TrackingCombination(tagId, checkIn, checkOut);
        trackingCombinations.add(trackingCombination);

        return trackingCombinations;
    }

    public List<TrackingCombination> mergeTrackingCombination(List<TrackingCombination> draftData,
                                                              List<TrackingCombination> existingData, String tagId, Date date) {

        if (CollectionUtils.isEmpty(existingData)) {
            return trackingCombinationRepository.saveAll(draftData);
        }

        draftData.addAll(existingData);
        List<TrackingCombination> mergedTrackingCombination = mergeTrackingCombination(draftData);

        trackingCombinationRepository.deleteByTagIdDate(tagId, date);
        return trackingCombinationRepository.saveAll(mergedTrackingCombination);
    }


    public List<TrackingCombination> mergeTrackingCombination(List<TrackingCombination> trackingCombinations) {
        if (CollectionUtils.isEmpty(trackingCombinations) || trackingCombinations.size() == 1) {
            return trackingCombinations;
        }

        trackingCombinations.sort(Comparator.comparing(TrackingCombination::getCheckIn));

        List<TrackingCombination> mergedTrackingCb = new ArrayList<>();
        TrackingCombination currentTrackingCb = trackingCombinations.get(0);

        for (int i = 1; i < trackingCombinations.size(); i++) {
            TrackingCombination nextTrackingCombination = trackingCombinations.get(i);

            if (currentTrackingCb.getCheckOut().compareTo(nextTrackingCombination.getCheckIn()) >= 0) {
                if (currentTrackingCb.getCheckOut().compareTo(nextTrackingCombination.getCheckOut()) < 0) {
                    currentTrackingCb.setCheckOut(nextTrackingCombination.getCheckOut());
                }
            } else {
                mergedTrackingCb.add(currentTrackingCb);
                currentTrackingCb = nextTrackingCombination;
            }
        }

        // Add the last tracking combination
        mergedTrackingCb.add(currentTrackingCb);

        return mergedTrackingCb;
    }
}
