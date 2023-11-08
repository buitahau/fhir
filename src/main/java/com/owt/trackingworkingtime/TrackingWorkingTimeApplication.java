package com.owt.trackingworkingtime;

import com.owt.trackingworkingtime.service.mqtt.MessagingService;
import com.owt.trackingworkingtime.util.DateUtil;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Date;

@SpringBootApplication(scanBasePackages = "com.owt.trackingworkingtime")
@EnableScheduling
public class TrackingWorkingTimeApplication {

    @Autowired
    private MessagingService messagingService;

    private static final String TRACING_TOPIC = "tracing";

    public static void main(String[] args) {
        SpringApplication.run(TrackingWorkingTimeApplication.class, args);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void subscribeTopic() throws MqttException {
        messagingService.subscribe(TRACING_TOPIC);
    }

    @Scheduled(fixedRate = 5000)
    public void publishTag001() throws MqttException {
        publish("000001");
    }

    @Scheduled(fixedRate = 3000)
    public void publishTag002() throws MqttException {
        publish("000002");
    }

    @Scheduled(fixedRate = 10000)
    public void publishTag003() throws MqttException {
        publish("000003");
    }

    private void publish(String tagId) throws MqttException {
        messagingService.publish(TRACING_TOPIC, tagId + "/" + DateUtil.convert(new Date()));
    }
}
