package com.networknt.subscription.schema.models;

/**
 * Created by Nicholas Azar on October 17, 2017.
 */
public class MessageInput {
    private int channelId;
    private String text;

    public MessageInput(int channelId, String text) {
        this.channelId = channelId;
        this.text = text;
    }

    public int getChannelId() {
        return channelId;
    }

    public void setChannelId(int channelId) {
        this.channelId = channelId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
