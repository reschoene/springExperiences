package reschoene.springOnDocker.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {
    @GetMapping("/")
    public String getWellcomeMessage(){
        return "Hello!!!";
    }
}
