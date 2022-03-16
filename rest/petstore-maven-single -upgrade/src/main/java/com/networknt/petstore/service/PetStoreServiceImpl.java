package com.networknt.petstore.service;

import com.networknt.petstore.model.Pet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class PetStoreServiceImpl implements PetStoreService{
    private static final Logger logger = LoggerFactory.getLogger(PetStoreService.class);

    @Override
    public List<Pet> getPets(){
        List<Pet> pets = new ArrayList<>();
        pets.add(generatePet(1L, "catten", "cat"));
        pets.add(generatePet(2L, "doggy", "dog"));
        return pets;
    }

    @Override
    public Pet deletePetById(Long id){
        return generatePet(id, "deleted", "delete_pet");
    }

    @Override
    public Pet getPetById(Long id){
        return generatePet(id, "deleted", "delete_pet");
    }

    @Override
    public void addPet(Pet pet){
         logger.info("save pet to pet store:" + pet.getName());
    }

    private Pet generatePet(Long id, String name, String tag) {
        Pet pet = new Pet();
        pet.setId(id);
        pet.setName(name);
        pet.setTag(tag);
        return pet;
    }
}
