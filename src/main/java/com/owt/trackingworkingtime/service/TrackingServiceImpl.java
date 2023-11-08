package com.owt.trackingworkingtime.service;

import com.owt.trackingworkingtime.model.Tracking;
import com.owt.trackingworkingtime.model.TrackingId;
import com.owt.trackingworkingtime.repository.TrackingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TrackingServiceImpl implements TrackingService {

    @Autowired
    TrackingRepository trackingRepository;

    @Override
    public Tracking save(Tracking tracking) {
        TrackingId trackingId = new TrackingId(tracking.getTagId(), tracking.getTrackingTime());

        Optional<Tracking> optionalTracking = trackingRepository.findById(trackingId);
        if (optionalTracking.isPresent()) {
            return optionalTracking.get();
        }

        return trackingRepository.save(tracking);
    }
}
