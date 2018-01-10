package com.networknt.subscription.schema;

import com.networknt.subscription.schema.models.Channel;
import com.networknt.subscription.schema.models.Message;
import com.networknt.subscription.schema.models.MessageAddedEvent;
import com.networknt.subscription.schema.models.MessageInput;
import graphql.schema.DataFetcher;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.ArrayList;

/**
 * Created by Nicholas Azar on October 17, 2017.
 */
public class SubscriptionWiring {

    final static ChannelPublisher channelPublisher = new ChannelPublisher();

    public static class Context {
        final List<Channel> channels = new ArrayList<>();

        public Context() {}

        public List<Channel> getChannels() {
            return this.channels;
        }

        public Channel getChannel(int id) {
            for (Channel channel : this.channels) {
                if (channel.getId() == id) {
                    return channel;
                }
            }
            return null;
        }

        public Channel addChannel(Channel channel) {
            channel.setId(this.channels.size() + 1);
            this.channels.add(channel);
            return channel;
        }
    }

    private static SubscriptionWiring.Context context = new SubscriptionWiring.Context();

    static {
        context.addChannel(new Channel("Channel1"));
        context.addChannel(new Channel("Channel2"));
        context.addChannel(new Channel("Channel3"));

        context.getChannel(1).addMessage(new Message(1, "Channel1Message1"));
        context.getChannel(1).addMessage(new Message(2, "Channel1Message2"));
    }

    static DataFetcher channelsFetcher = dataFetchingEnvironment -> context.getChannels();
    static DataFetcher channelFetcher = dataFetchingEnvironment -> context.getChannel(Integer.valueOf(dataFetchingEnvironment.getArgument("id")));

    static DataFetcher addChannelFetcher = dataFetchingEnvironment ->
            context.addChannel(new Channel(dataFetchingEnvironment.getArgument("name")));

    static DataFetcher addMessageFetcher = dataFetchingEnvironment -> {
        LinkedHashMap<String, String> arguments = dataFetchingEnvironment.getArgument("message");
        MessageInput messageInput = new MessageInput(Integer.valueOf(arguments.get("channelId")), arguments.get("text"));
        Channel channel = context.getChannel(messageInput.getChannelId());
        int messageId = channel.getMessages().size() + 1;

        Message message = new Message(messageId, messageInput.getText());
        channel.addMessage(message);
        channelPublisher.onMessageAdded(new MessageAddedEvent(messageInput.getChannelId(), message));
        return message;
    };

    static DataFetcher messageAddedFetcher = dataFetchingEnvironment -> {
        int channelId = Integer.valueOf(dataFetchingEnvironment.getArgument("channelId"));
        return channelPublisher.getPublisher(channelId);
    };
}
