package reschoene.springExperiences.SpringKafka.controllers;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reschoene.springExperiences.SpringKafka.services.KafKaProducerService;

@RestController
@RequestMapping(value = "/kafka")
@AllArgsConstructor
public class KafkaProducerController
{
    private final KafKaProducerService producerService;

    @GetMapping(value = "/publish")
    public void sendMessageToKafkaTopic(@RequestParam("message") String message)
    {
        this.producerService.sendMessage(message);
    }
}
