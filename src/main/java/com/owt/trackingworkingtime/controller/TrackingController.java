package com.owt.trackingworkingtime.controller;

import com.owt.trackingworkingtime.dto.TrackingDto;
import com.owt.trackingworkingtime.service.TrackingService;
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

    @GetMapping
    public List<TrackingDto> find(@RequestParam(value = "tagId") String tadId,
                                  @RequestParam(value = "date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date date) {

        return trackingService.find(tadId, date);
    }
}
