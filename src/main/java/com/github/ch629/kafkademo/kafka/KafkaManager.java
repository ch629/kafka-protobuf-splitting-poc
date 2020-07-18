package com.github.ch629.kafkademo.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.stereotype.Component;

@Component
public class KafkaManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaManager.class);

    private final KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry;

    public KafkaManager(final KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry) {
        this.kafkaListenerEndpointRegistry = kafkaListenerEndpointRegistry;
    }

    public void pause() {
        LOGGER.info("Pausing all listeners");
        kafkaListenerEndpointRegistry.getListenerContainers().forEach(it -> {
            LOGGER.info("Pausing: {}", it.getListenerId());
            it.pause();
        });
    }

    public void resume() {
        LOGGER.info("Resuming all listeners");
        kafkaListenerEndpointRegistry.getListenerContainers().forEach(it -> {
            LOGGER.info("Resuming: {}", it.getListenerId());
            it.resume();
        });
    }
}
