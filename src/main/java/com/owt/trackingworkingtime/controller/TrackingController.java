package com.owt.trackingworkingtime.controller;

import com.owt.trackingworkingtime.dto.TrackingDto;
import com.owt.trackingworkingtime.dto.TrackingRequestDto;
import com.owt.trackingworkingtime.dto.TrackingResponseDto;
import com.owt.trackingworkingtime.service.TrackingCombinationService;
import com.owt.trackingworkingtime.service.TrackingService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
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

    @GetMapping("/aggregate")
    public String aggregate(@RequestParam(value = "tagId") String tadId,
                            @RequestParam(value = "date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date date) {
        trackingCombinationService.aggregateTracking(tadId, date);
        return "Aggregate successfully!";
    }

    @GetMapping("/tag-ids-online")
    public List<String> findTagIdsOnline(@RequestParam(value = "date", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date date) {
        return trackingService.findTagIdsByDatetime(date);
    }

    @PostMapping("/list-tracking-combination")
    public List<TrackingResponseDto> findTrackingCombinations(@RequestBody @Valid TrackingRequestDto trackingRequestDto) {
        return trackingCombinationService.findByTagIdsAndDate(trackingRequestDto);
    }

    @PostMapping("/list-tracking")
    public List<TrackingDto> findTracking(@RequestBody @Valid TrackingRequestDto trackingRequestDto) {
        return trackingService.find(trackingRequestDto);
    }

    @PostMapping("/save-tracking")
    public TrackingDto saveTracking(@RequestBody @Valid TrackingDto trackingDto) {
        return trackingService.save(trackingDto);
    }
}
