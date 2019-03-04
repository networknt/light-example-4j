package com.networknt.demo;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GreetingController {
    @RequestMapping("/spring-boot")
    public String greeting() {
        return "OK";
    }
}
