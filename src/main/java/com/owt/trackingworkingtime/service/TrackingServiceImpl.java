package com.owt.trackingworkingtime.service;

import com.owt.trackingworkingtime.model.Tracking;
import com.owt.trackingworkingtime.repository.TrackingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TrackingServiceImpl implements TrackingService {

    @Autowired
    TrackingRepository trackingRepository;

    @Override
    public Tracking save(Tracking tracking) {
        return trackingRepository.save(tracking);
    }
}
