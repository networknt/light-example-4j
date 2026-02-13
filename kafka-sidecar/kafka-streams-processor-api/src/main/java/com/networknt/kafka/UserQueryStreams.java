package com.networknt.kafka;

import com.networknt.config.Config;
import com.networknt.kafka.common.config.KafkaStreamsConfig;
import com.networknt.kafka.streams.KafkaStreamsRegistry;
import com.networknt.kafka.streams.LightStreams;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StoreQueryParameters;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.processor.AbstractProcessor;
import org.apache.kafka.streams.processor.ProcessorContext;
import org.apache.kafka.streams.state.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.Properties;

public class UserQueryStreams implements LightStreams {
    static private final Logger logger = LoggerFactory.getLogger(UserQueryStreams.class);
    private static final String APP = "userQuery";

    static private Properties streamsProps;
    static final KafkaStreamsConfig config = (KafkaStreamsConfig) Config.getInstance().getJsonObjectConfig(KafkaStreamsConfig.CONFIG_NAME, KafkaStreamsConfig.class);

    static {
        streamsProps = new Properties();
        streamsProps.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, config.getProperties().getBootstrapServers());
        streamsProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
    }

    private static final String userId = "user-id-store"; // this is a local store from userId to user object.

    KafkaStreams userQueryStreams;

    public UserQueryStreams() {
        logger.info("UserQueryStreams is created");
    }

    public ReadOnlyKeyValueStore<String, String> getUserIdStore() {
        return userQueryStreams.store(StoreQueryParameters.fromNameAndType(userId, QueryableStoreTypes.keyValueStore()));
    }

    public org.apache.kafka.streams.KeyQueryMetadata getUserIdStreamsMetadata(String id) {
        return userQueryStreams.queryMetadataForKey(userId,  id, Serdes.String().serializer());
    }

    private void startUserQueryStreams(String ip, int port) {

        StoreBuilder<KeyValueStore<String, String>> keyValueUserIdStoreBuilder =
                Stores.keyValueStoreBuilder(Stores.persistentKeyValueStore(userId),
                        Serdes.String(),
                        Serdes.String());

        final Topology topology = new Topology();
        topology.addSource("SourceTopicProcessor", "test");
        topology.addProcessor("UserEventProcessor", UserEventProcessor::new, "SourceTopicProcessor");
        topology.addStateStore(keyValueUserIdStoreBuilder, "UserEventProcessor");

        streamsProps.put(StreamsConfig.APPLICATION_ID_CONFIG, "user-query");
        streamsProps.put(StreamsConfig.APPLICATION_SERVER_CONFIG, ip + ":" + port);
        userQueryStreams = new KafkaStreams(topology, streamsProps);
        if(config.getCleanUp()) {
            userQueryStreams.cleanUp();
        }
        userQueryStreams.start();
        KafkaStreamsRegistry.register("UserQueryStreams", userQueryStreams);
    }

    public static class UserEventProcessor extends AbstractProcessor<byte[], byte[]> {

        private ProcessorContext pc;
        private KeyValueStore<String, String> userIdStore;

        public UserEventProcessor() {
        }

        @Override
        public void init(ProcessorContext pc) {
            this.pc = pc;
            this.userIdStore = (KeyValueStore<String, String>) pc.getStateStore(userId);
            if (logger.isInfoEnabled()) logger.info("Processor initialized");
        }

        @Override
        public void process(byte[] key, byte[] value) {
            String userId = new String(key, StandardCharsets.UTF_8);
            String userStr = new String(value, StandardCharsets.UTF_8);
            if(logger.isTraceEnabled()) logger.trace("Event = " + userStr);
            userIdStore.put(userId, userStr);
        }
    }

    @Override
    public void start(String ip, int port) {
        if(logger.isDebugEnabled()) logger.debug("userQueryStreams is starting...");
        startUserQueryStreams(ip, port);
    }

    @Override
    public void close() {
        if(logger.isDebugEnabled()) logger.debug("userQueryStreams is closing...");
        userQueryStreams.close();
    }
}
