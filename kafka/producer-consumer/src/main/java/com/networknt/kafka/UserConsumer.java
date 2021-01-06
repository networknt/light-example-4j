package com.networknt.kafka;

import com.networknt.config.JsonMapper;
import com.networknt.kafka.consumer.AbstractConsumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.*;

public class UserConsumer extends AbstractConsumer {
    public void subscribe(String topic) {
        consumer.subscribe(Arrays.asList(topic));
    }

    public List<Map<String, Object>> poll() {
        List<Map<String, Object>> list = new ArrayList<>();
        ConsumerRecords<byte[], byte[]> records = consumer.poll(Duration.ofMillis(100));
        for(ConsumerRecord<byte[], byte[]> record: records) {
            String value = new String(record.value(), StandardCharsets.UTF_8);
            Map<String, Object> userMap = JsonMapper.string2Map(value);
            userMap.put("partition", record.partition());
            userMap.put("offset", record.offset());
            list.add(userMap);
        }
        return list;
    }
}
