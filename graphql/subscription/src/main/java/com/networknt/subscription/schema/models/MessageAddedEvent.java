package com.networknt.subscription.schema.models;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by Nicholas Azar on October 18, 2017.
 */
@Data
@AllArgsConstructor
public class MessageAddedEvent {
    private int channelId;
    private Message message;
}
