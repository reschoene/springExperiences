package br.com.reschoene.JDBCBasicAuth.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    @GetMapping("/")
    String hello(){
        return "Oie JDBC!!!";
    }
}
