package com.owt.trackingworkingtime.controller;

import com.owt.trackingworkingtime.dto.TrackingCombinationDto;
import com.owt.trackingworkingtime.dto.TrackingDto;
import com.owt.trackingworkingtime.dto.TrackingRequestDto;
import com.owt.trackingworkingtime.model.TrackingCombination;
import com.owt.trackingworkingtime.service.TrackingCombinationService;
import com.owt.trackingworkingtime.service.TrackingService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tracking")
public class TrackingController {

    @Autowired
    private TrackingService trackingService;

    @Autowired
    private TrackingCombinationService trackingCombinationService;

    @PostMapping
    public List<TrackingDto> find(@RequestBody @Valid TrackingRequestDto trackingRequestDto) {
        return trackingService.find(trackingRequestDto);
    }

    @PostMapping("/test")
    public List<TrackingCombination> test(@RequestBody @Valid TrackingCombinationDto trackingCombinationDto) {
        return trackingCombinationService.optimizeTrackingTime(trackingCombinationDto);
    }
}
