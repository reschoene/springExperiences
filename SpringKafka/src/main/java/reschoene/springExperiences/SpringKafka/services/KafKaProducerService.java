package reschoene.springExperiences.SpringKafka.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafKaProducerService
{
    @Value("${topic.name}")
    private String topicName;

    private static final Logger logger =
            LoggerFactory.getLogger(KafKaProducerService.class);

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public void sendMessage(String message)
    {
        logger.info(String.format("Message sent -> %s", message));
        this.kafkaTemplate.send(topicName, message);
    }
}
