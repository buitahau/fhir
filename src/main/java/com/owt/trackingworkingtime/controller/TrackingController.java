package com.owt.trackingworkingtime.controller;

import com.owt.trackingworkingtime.dto.TrackingDto;
import com.owt.trackingworkingtime.model.Tracking;
import com.owt.trackingworkingtime.service.TrackingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/tracking")
public class TrackingController {
    @Autowired
    TrackingService trackingService;

    @PostMapping
    public ResponseEntity<Tracking> save(@RequestBody TrackingDto trackingDto) {
        Tracking tracking = trackingService.save(trackingDto.toTracking());
        return new ResponseEntity<>(tracking, HttpStatus.CREATED);
    }
}
