package com.networknt.servlet;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CreatePetsController {
    @RequestMapping(path="/spring/pets", method= RequestMethod.POST)
    public String createPets(@RequestBody String body) {
        return body;
    }
}
