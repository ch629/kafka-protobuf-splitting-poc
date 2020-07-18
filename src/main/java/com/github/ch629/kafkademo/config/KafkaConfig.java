package com.github.ch629.kafkademo.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaOperations;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.SeekToCurrentErrorHandler;
import org.springframework.util.backoff.FixedBackOff;

@Configuration
public class KafkaConfig {
    @Bean
    public NewTopic topic() {
        return new NewTopic("Test", 1, (short) 1);
    }

    @Bean
    public NewTopic dlqTopic() {
        return new NewTopic("DLQ", 1, (short) 1);
    }

    @Bean
    public SeekToCurrentErrorHandler errorHandler(final KafkaOperations<Object, Object> template) {
        return new SeekToCurrentErrorHandler(new DeadLetterPublishingRecoverer(template), new FixedBackOff(1000L, 2));
    }
}
