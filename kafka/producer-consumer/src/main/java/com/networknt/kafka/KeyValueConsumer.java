package com.networknt.kafka;

import com.networknt.kafka.consumer.AbstractConsumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.*;

public class KeyValueConsumer extends AbstractConsumer {
    public void subscribe(String topic) {
        consumer.subscribe(Arrays.asList(topic));
    }

    public List<Map<String, Object>> poll() {
        List<Map<String, Object>> list = new ArrayList<>();
        ConsumerRecords<byte[], byte[]> records = consumer.poll(Duration.ofMillis(100));
        for(ConsumerRecord<byte[], byte[]> record: records) {
            String key = new String(record.key(), StandardCharsets.UTF_8);
            String value = new String(record.value(), StandardCharsets.UTF_8);
            Map<String, Object> map = new HashMap<>();
            map.put("key", key);
            map.put("value", value);
            map.put("partition", record.partition());
            map.put("offset", record.offset());
            list.add(map);
        }
        return list;
    }
}
