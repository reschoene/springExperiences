package reschoene.springExperiences.SpringKafka.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafKaConsumerService
{
    private final Logger logger =
            LoggerFactory.getLogger(KafKaConsumerService.class);

    @KafkaListener(topics = "${topic.name}", groupId = "${spring.kafka.consumer.group-id}")
    public void consume(String message)
    {
        logger.info(String.format("Message recieved -> %s", message));
    }
}
