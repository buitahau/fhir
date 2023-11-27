package com.owt.trackingworkingtime.controller;

import com.owt.trackingworkingtime.dto.TrackingCombinationDto;
import com.owt.trackingworkingtime.service.TrackingCombinationService;
import com.owt.trackingworkingtime.service.TrackingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/api/v1/tracking")
public class TrackingController {

    @Autowired
    private TrackingService trackingService;

    @Autowired
    private TrackingCombinationService trackingCombinationService;

    @GetMapping("/tag-ids-online")
    public List<String> findTagIdsOnline(@RequestParam(value = "date", required = false)
                                         @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date date) {
        if (Objects.isNull(date)) date = new Date();
        return trackingService.findTagIdsByDatetime(date);
    }

    @GetMapping("/tracking-combinations")
    public Map<String, List<TrackingCombinationDto>> findTrackingCombinations(
            @RequestParam(value = "tag-ids", required = false) List<String> tagIds,
            @RequestParam(value = "check-in") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date checkIn,
            @RequestParam(value = "check-out") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date checkOut) {
        return trackingCombinationService.findByTagIdsAndDate(tagIds, checkIn, checkOut);
    }
}
