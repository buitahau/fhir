package com.owt.trackingworkingtime.service.mqtt;

import com.owt.trackingworkingtime.dto.TrackingDto;
import com.owt.trackingworkingtime.service.TrackingService;
import com.owt.trackingworkingtime.util.DateUtil;
import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Date;

@Service
public class MessagingService {

    @Autowired
    private IMqttClient mqttClient;

    @Autowired
    private TrackingService trackingService;

    public void publish(final String topic, final String payload) throws MqttException {
        MqttMessage mqttMessage = new MqttMessage();
        mqttMessage.setPayload(payload.getBytes());
        mqttMessage.setQos(0);
        mqttMessage.setRetained(true);

        mqttClient.publish(topic, mqttMessage);
    }

    public void subscribe(final String tpic) throws MqttException {
        mqttClient.subscribeWithResponse(tpic, (topic, msg) -> {
            System.out.println("Receive: " + msg.getId() + " -> " + new String(msg.getPayload()));
            save(new String(msg.getPayload()));
        });
    }

    private void save(String payload) {
        try {
            String[] data = payload.split("/");
            String tagId = data[0];
            Date trackingTime = DateUtil.convert(data[1]);

            TrackingDto trackingDto = new TrackingDto();
            trackingDto.setTagId(tagId.trim());
            trackingDto.setTrackingTime(trackingTime);

            trackingService.save(trackingDto);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
