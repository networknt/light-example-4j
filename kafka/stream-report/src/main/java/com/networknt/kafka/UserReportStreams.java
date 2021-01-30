package com.networknt.kafka;

import com.networknt.config.Config;
import com.networknt.config.JsonMapper;
import com.networknt.kafka.common.KafkaStreamsConfig;
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
import java.util.Map;
import java.util.Properties;

public class UserReportStreams implements LightStreams {

    static private final Logger logger = LoggerFactory.getLogger(UserReportStreams.class);
    private static final String APP = "userReport";

    static private Properties streamsProps;
    static final KafkaStreamsConfig config = (KafkaStreamsConfig) Config.getInstance().getJsonObjectConfig(KafkaStreamsConfig.CONFIG_NAME, KafkaStreamsConfig.class);

    static {
        streamsProps = new Properties();
        streamsProps.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, config.getBootstrapServers());
        streamsProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
    }

    private static final String countryUsers = "country-users-store"; // this is a local store from country to a list of users

    KafkaStreams userReportStreams;

    public UserReportStreams() {
        logger.info("UserReportStreams is created");
    }

    public ReadOnlyKeyValueStore<String, String> getCountryUsersStore() {
        return userReportStreams.store(StoreQueryParameters.fromNameAndType(countryUsers, QueryableStoreTypes.keyValueStore()));
    }

    public StreamsMetadata getCountryUsersStreamsMetadata(String country) {
        return userReportStreams.metadataForKey(countryUsers,  country, Serdes.String().serializer());
    }

    private void startUserReportStreams(String ip, int port) {

        StoreBuilder<KeyValueStore<String, String>> keyValueCountryUsersStoreBuilder =
                Stores.keyValueStoreBuilder(Stores.persistentKeyValueStore(countryUsers),
                        Serdes.String(),
                        Serdes.String());

        final Topology topology = new Topology();
        topology.addSource("SourceTopicProcessor", "test");
        topology.addProcessor("UserEventProcessor", UserEventProcessor::new, "SourceTopicProcessor");
        topology.addStateStore(keyValueCountryUsersStoreBuilder, "UserEventProcessor");

        streamsProps.put(StreamsConfig.APPLICATION_ID_CONFIG, "user-report");
        streamsProps.put(StreamsConfig.APPLICATION_SERVER_CONFIG, ip + ":" + port);
        userReportStreams = new KafkaStreams(topology, streamsProps);
        if(config.isCleanUp()) {
            userReportStreams.cleanUp();
        }
        userReportStreams.start();
    }

    public static class UserEventProcessor extends AbstractProcessor<byte[], byte[]> {

        private ProcessorContext pc;
        private KeyValueStore<String, String> countryUsersStore;

        public UserEventProcessor() {
        }

        @Override
        public void init(ProcessorContext pc) {
            this.pc = pc;
            this.countryUsersStore = (KeyValueStore<String, String>) pc.getStateStore(countryUsers);
            if (logger.isInfoEnabled()) logger.info("Processor initialized");
        }

        @Override
        public void process(byte[] key, byte[] value) {
            String userId = new String(key, StandardCharsets.UTF_8);
            String userStr = new String(value, StandardCharsets.UTF_8);
            if(logger.isTraceEnabled()) logger.trace("Event = " + userStr);
            Map<String, Object> userMap = JsonMapper.string2Map(userStr);
            String country = (String)userMap.get("country");
            String countStr = countryUsersStore.get(country);
            if(countStr != null) {
                Integer count = Integer.valueOf(countStr);
                count ++;
            } else {
                Integer count = 1;
                countryUsersStore.put(country, count.toString());
            }
        }
    }

    @Override
    public void start(String ip, int port) {
        if(logger.isDebugEnabled()) logger.debug("userReportStreams is starting...");
        startUserReportStreams(ip, port);
    }

    @Override
    public void close() {
        if(logger.isDebugEnabled()) logger.debug("userReportStreams is closing...");
        userReportStreams.close();
    }
}
