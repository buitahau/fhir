package owt.training.fhir.config;

import com.microsoft.azure.spring.autoconfigure.jms.ConnectionStringResolver;
import com.microsoft.azure.spring.autoconfigure.jms.ServiceBusKey;
import org.apache.qpid.jms.JmsConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;

import javax.jms.ConnectionFactory;

@Configuration
public class ServiceBusJmsConfiguration {

    @Value("${spring.jms.servicebus.connection-string}")
    private String connectionString;

    @Value("${spring.jms.servicebus.idle-timeout}")
    private int idleTimeout;

    @Bean
    public ConnectionFactory connectionFactory() {
        ServiceBusKey serviceBusKey = ConnectionStringResolver.getServiceBusKey(connectionString);
        String host = serviceBusKey.getHost();
        String sasKeyName = serviceBusKey.getSharedAccessKeyName();
        String sasKey = serviceBusKey.getSharedAccessKey();
        String remoteUri = String.format("amqps://%s?amqp.idleTimeout=%d", host, idleTimeout);
        JmsConnectionFactory jmsConnectionFactory = new JmsConnectionFactory();
        jmsConnectionFactory.setRemoteURI(remoteUri);
        jmsConnectionFactory.setUsername(sasKeyName);
        jmsConnectionFactory.setPassword(sasKey);
        return jmsConnectionFactory;
    }

    @Bean
    public JmsListenerContainerFactory<?> jmsListenerContainerFactory(ConnectionFactory connectionFactory) {
        DefaultJmsListenerContainerFactory jmsListenerContainerFactory = new DefaultJmsListenerContainerFactory();
        jmsListenerContainerFactory.setConnectionFactory(connectionFactory);
        return jmsListenerContainerFactory;
    }

    @Bean
    public JmsTemplate jmsTemplate(ConnectionFactory jmsConnectionFactory) {
        JmsTemplate jmsTemplate = new JmsTemplate();
        jmsTemplate.setConnectionFactory(jmsConnectionFactory);
        return jmsTemplate;
    }

    @Bean
    public JmsListenerContainerFactory<?> topicJmsListenerContainerFactory(ConnectionFactory connectionFactory) {
        DefaultJmsListenerContainerFactory jmsListenerContainerFactory = new DefaultJmsListenerContainerFactory();
        jmsListenerContainerFactory.setConnectionFactory(connectionFactory);
        jmsListenerContainerFactory.setSubscriptionDurable(Boolean.TRUE);
        return jmsListenerContainerFactory;
    }
}
