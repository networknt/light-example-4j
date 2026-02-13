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
import org.apache.kafka.streams.errors.StreamsUncaughtExceptionHandler;
import org.apache.kafka.streams.processor.AbstractProcessor;
import org.apache.kafka.streams.processor.ProcessorContext;
import org.apache.kafka.streams.state.*;
import org.checkerframework.checker.units.qual.K;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.Properties;

public class WordCountStreams implements LightStreams {
    static private final Logger logger = LoggerFactory.getLogger(WordCountStreams.class);
    static final KafkaStreamsConfig config = (KafkaStreamsConfig) Config.getInstance().getJsonObjectConfig(KafkaStreamsConfig.CONFIG_NAME, KafkaStreamsConfig.class);
    private KafkaStreams wordCountStreams;

    public WordCountStreams() {
        logger.info("WordCountStreams is created");
    }

    @Override
    public void start(String ip, int port) {
        if(logger.isDebugEnabled()) logger.debug("WordCountStreams is starting...");
        Properties streamsProps = new Properties();
        streamsProps.putAll(config.getKafkaMapProperties());
        streamsProps.put(StreamsConfig.APPLICATION_SERVER_CONFIG, ip + ":" + port);
        streamsProps.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        streamsProps.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        WordCountStreamsTopology topology = new WordCountStreamsTopology();
        wordCountStreams = new KafkaStreams(topology.build(), streamsProps);
        wordCountStreams.setUncaughtExceptionHandler(eh -> {
            logger.error("Kafka-Streams uncaught exception occurred. Streams app will be replaced with a new thread", eh);
            return StreamsUncaughtExceptionHandler.StreamThreadExceptionResponse.REPLACE_THREAD;
        });
        if(config.getCleanUp()) {
            wordCountStreams.cleanUp();
        }
        wordCountStreams.start();
        KafkaStreamsRegistry.register("WordCountStreams", wordCountStreams);
        if(logger.isDebugEnabled()) logger.debug("WordCountStreams is started.");
    }

    @Override
    public void close() {
        if(logger.isDebugEnabled()) logger.debug("WordCountStreams is closing...");
        wordCountStreams.close();
    }

    public KafkaStreams getKafkaStreams() {
        return wordCountStreams;
    }
}
