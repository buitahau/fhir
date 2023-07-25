package owt.training.fhir.message_queue.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import owt.training.fhir.message_queue.MessageQueueService;

import javax.jms.*;
import java.io.Serializable;

@Service
public class MessageQueueServiceImpl implements MessageQueueService {

    @Autowired
    private ConnectionFactory connectionFactory;

    @Override
    public void sendToQueue(String queueName, Serializable object) {
        try (JMSContext jmsContext = connectionFactory.createContext()) {
            Queue queue = jmsContext.createQueue(queueName);
            JMSProducer producer = jmsContext.createProducer();
            ObjectMessage msg = jmsContext.createObjectMessage(object);
            producer.send(queue, msg);
        }
    }
}
