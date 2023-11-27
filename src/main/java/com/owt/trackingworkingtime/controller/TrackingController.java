package com.owt.trackingworkingtime.controller;

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
import java.util.Objects;

@RestController
@RequestMapping("/api/v1/tracking")
public class TrackingController {

    @Autowired
    private TrackingService trackingService;

    @Autowired
    private TrackingCombinationService trackingCombinationService;

    @GetMapping("/tag-ids-online")
    public List<String> findTagIdsOnline(@RequestParam(value = "date", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date date) {
        if (Objects.isNull(date)) date = new Date();
        return trackingService.findTagIdsByDatetime(date);
    }

    @PostMapping("/list-tracking-combination")
    public List<TrackingResponseDto> findTrackingCombinations(@RequestBody @Valid TrackingRequestDto trackingRequestDto) {
        return trackingCombinationService.findByTagIdsAndDate(trackingRequestDto);
    }
}
