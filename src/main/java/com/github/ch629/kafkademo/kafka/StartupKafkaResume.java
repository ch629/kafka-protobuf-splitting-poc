package com.github.ch629.kafkademo.kafka;

import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.stereotype.Component;

@Component
public class StartupKafkaResume implements ApplicationListener<WebServerInitializedEvent> {
    private final KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry;

    public StartupKafkaResume(KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry) {
        this.kafkaListenerEndpointRegistry = kafkaListenerEndpointRegistry;
    }

    @Override
    public void onApplicationEvent(final WebServerInitializedEvent webServerInitializedEvent) {
        // TODO: Check redis whether to pause consumers before starting the registry
        kafkaListenerEndpointRegistry.start();
    }
}
