package reschoene.springKafkaProducer.controllers;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reschoene.springKafkaProducer.services.KafkaProducerService;

@RestController
@RequestMapping(value = "/kafka")
@AllArgsConstructor
public class KafkaProducerController
{
    private final KafkaProducerService producerService;

    @GetMapping(value = "/publish")
    public void sendMessageToKafkaTopic(@RequestParam("message") String message)
    {
        this.producerService.sendMessage(message);
    }
}
