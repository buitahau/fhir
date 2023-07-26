package owt.training.fhir.message_queue.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import owt.training.fhir.constant.QueueNameConstant;
import owt.training.fhir.message_queue.dto.FhirMessage;
import owt.training.fhir.util.JsonUtil;

@Component
public class ResourceResponseListener {

    private static final Logger log = LoggerFactory.getLogger(ResourceResponseListener.class);

    @JmsListener(destination = QueueNameConstant.RESOURCE_RESPONSE,
            containerFactory = "jmsListenerContainerFactory")
    public void receiveMessage(FhirMessage message) {
        log.info("Receive resource response: " + JsonUtil.toString(message));
    }
}
