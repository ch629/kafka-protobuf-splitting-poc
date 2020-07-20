package com.github.ch629.kafkademo.kafka;

import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.stereotype.Component;

@Component
public class StartupInternalStateLoader implements ApplicationListener<WebServerInitializedEvent> {
    private final KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry;
    private final KafkaManager kafkaManager;

    public StartupInternalStateLoader(final KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry, final KafkaManager kafkaManager) {
        this.kafkaListenerEndpointRegistry = kafkaListenerEndpointRegistry;
        this.kafkaManager = kafkaManager;
    }

    @Override
    public void onApplicationEvent(final WebServerInitializedEvent unused) {
        kafkaManager.registerListener(kafkaListenerEndpointRegistry);
    }
}
