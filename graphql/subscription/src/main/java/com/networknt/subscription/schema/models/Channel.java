package com.networknt.subscription.schema.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nicholas Azar on October 17, 2017.
 */
public class Channel {
    private int id;
    private String name;
    private List<Message> messages = new ArrayList<>();

    public Channel(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public void addMessage(Message message) {
        this.messages.add(message);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
