package owt.training.fhir.message_queue.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import owt.training.fhir.constant.QueueNameConstant;
import owt.training.fhir.domain.PatientEntity;

@Component
public class FhirTestListener {

    private static final Logger log = LoggerFactory.getLogger(FhirTestListener.class);

    @JmsListener(destination = QueueNameConstant.FHIR_PREMIUM, containerFactory = "jmsListenerContainerFactory")
    public void receiveMessage(PatientEntity message) {
        log.info("Receive patient: " + message);
    }
}
