package reschoene.springKafkaProducer.controllers;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reschoene.springKafkaProducer.services.KafkaProducerService;

@RestController
@AllArgsConstructor
public class KafkaProducerController
{
    private final KafkaProducerService producerService;

    @GetMapping(value = "/send")
    public String sendMessage(@RequestParam("message") String message)
    {
        this.producerService.sendMessage(message);

        return "Message " + message + " was send!!";
    }
}
