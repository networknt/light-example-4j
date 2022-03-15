package com.networknt.petstore.service;

import com.networknt.petstore.model.Pet;

import java.util.List;

public interface PetStoreService {
    List<Pet> getPets();
    Pet deletePetById(Long id);
    Pet getPetById(Long id);
    void addPet(Pet pet);

}
