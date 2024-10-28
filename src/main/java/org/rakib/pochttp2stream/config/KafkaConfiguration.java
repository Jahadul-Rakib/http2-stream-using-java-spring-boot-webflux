package org.rakib.pochttp2stream.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Configuration;
import reactor.kafka.receiver.KafkaReceiver;
import reactor.kafka.receiver.ReceiverOptions;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


@Configuration
public class KafkaConfiguration {

    public KafkaReceiver<String, Object> createKafkaReceiver(String topic) {
        Map<String, Object> configProps = getKafkaConfiguration();
        ReceiverOptions<String, Object> receiverOptions = ReceiverOptions.<String, Object>create(configProps)
                .subscription(Collections.singleton(topic));
        return KafkaReceiver.create(receiverOptions);
    }

    private Map<String, Object> getKafkaConfiguration() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        configProps.put(ConsumerConfig.GROUP_ID_CONFIG, "stream-group-" + UUID.randomUUID());
        configProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        configProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        configProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest"); // latest/none/earliest

        // Add retention configurations
        // 12 hours retention for offsets
        configProps.put("offsets.retention.minutes", "720");
        // Check every 5 minutes
        configProps.put("offsets.retention.check.interval.ms", "300000");
        return configProps;
    }
}
