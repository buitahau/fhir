package owt.training.fhir.message_queue;

import java.io.Serializable;

public interface MessageQueueService {

    void sendToQueue(String queueName, Serializable object);
}
