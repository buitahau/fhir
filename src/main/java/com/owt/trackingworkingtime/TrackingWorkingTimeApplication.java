package com.owt.trackingworkingtime;

import com.owt.trackingworkingtime.service.CacheService;
import com.owt.trackingworkingtime.service.TrackingCombinationService;
import com.owt.trackingworkingtime.service.TrackingService;
import com.owt.trackingworkingtime.service.mqtt.MessagingService;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

@SpringBootApplication(scanBasePackages = "com.owt.trackingworkingtime")
@EnableScheduling
public class TrackingWorkingTimeApplication {

    private static final String TRACING_TOPIC = "tracing";

    @Autowired
    private MessagingService messagingService;

    @Autowired
    private CacheService cacheService;

    @Autowired
    private TrackingService trackingService;

    @Autowired
    private TrackingCombinationService trackingCombinationService;

    public static void main(String[] args) {
        SpringApplication.run(TrackingWorkingTimeApplication.class, args);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void subscribeTopic() throws MqttException {
        messagingService.subscribe(TRACING_TOPIC);
    }

    @Scheduled(fixedRateString = "${fixed-schedule.combine-caching}", initialDelayString = "${fixed-schedule.combine-caching}")
    public void aggregateTrackings() {
        Date currentDate = Calendar.getInstance(TimeZone.getDefault()).getTime();
        List<String> tagIdsByDate = trackingService.findTagIdsByDate(currentDate);
        for (String tagId : tagIdsByDate) {
            trackingCombinationService.aggregateTrackings(tagId, currentDate);
        }
        cacheService.evictAllCaches();
    }
}
