package owt.training.fhir.message_queue.listener;

import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.r4.model.IdType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import owt.training.fhir.constant.QueueNameConstant;
import owt.training.fhir.message_queue.MessageQueueService;
import owt.training.fhir.message_queue.dto.FhirMessage;
import owt.training.fhir.provider.PatientProvider;
import owt.training.fhir.util.JsonUtil;

@Component
public class ResourceRequestListener {

    private static final Logger log = LoggerFactory.getLogger(ResourceRequestListener.class);

    @Autowired
    private PatientProvider patientProvider;

    @Autowired
    private MessageQueueService messageQueueService;

    @JmsListener(destination = QueueNameConstant.RESOURCE_REQUEST,
            containerFactory = "jmsListenerContainerFactory")
    public void receiveMessage(FhirMessage message) {
        log.info("Receive resource request: " + JsonUtil.toString(message));

        IdType theId = new IdType().setValue(message.getBody().toString());

        FhirMessage response = FhirMessage
                .builder()
                .action(message.getAction())
                .resourceName(message.getResourceName())
                .body(patientProvider.findById(theId))
                .build();

        if (StringUtils.isNotBlank(message.getCallbackUrl())) {
            sendResponseToCallbackUrl(message.getCallbackUrl(), response);
            return;
        }
        messageQueueService.sendToQueue(QueueNameConstant.RESOURCE_RESPONSE, response);
    }

    private void sendResponseToCallbackUrl(String callbackUrl, FhirMessage response) {
        log.info("Send data to call back url");
    }
}
