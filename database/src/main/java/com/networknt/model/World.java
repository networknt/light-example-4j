package com.networknt.model;

/**
 * Created by steve on 22/10/16.
 */
public class World {
    public int id;
    public int randomNumber;

    /**
     * Constructs a new world object with the given parameters.
     *
     * @param id the ID of the world
     * @param randomNumber the random number of the world
     */
    public World(int id, int randomNumber) {
        this.id = id;
        this.randomNumber = randomNumber;
    }
}
