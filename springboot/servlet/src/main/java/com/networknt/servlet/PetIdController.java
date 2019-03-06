package com.networknt.servlet;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PetIdController {
    @RequestMapping("/spring/pets/{petId}")
    public String showPetById(@PathVariable String petId) {
        return "{\"id\":1,\"name\":\"Jessica Right\",\"tag\":\"pet\"}";
    }

    @DeleteMapping("/spring/pets/{petId}")
    public String deletePetById(@PathVariable String petId) {
        return "{\"id\":1,\"name\":\"Jessica Right\",\"tag\":\"pet\"}";
    }
}
