package com.github.ch629.kafkademo.kafka;

import com.github.ch629.kafkademo.domain.core.kafka.ListenerStatus;
import com.google.common.collect.ImmutableMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class KafkaManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaManager.class);

    private ImmutableMap<String, DecoratedKafkaListener> listeners;

    public void registerListener(final KafkaListenerEndpointRegistry messageListenerContainer) {
        if (listeners != null) {
            LOGGER.warn("Listeners already registered");
            return;
        }
        listeners = messageListenerContainer.getListenerContainers().stream().collect(ImmutableMap.toImmutableMap(MessageListenerContainer::getListenerId, DecoratedKafkaListener::new));
    }

    public void pauseAll(final boolean force) {
        LOGGER.info("Pausing all listeners");
        listeners.values().forEach(listener -> listener.pause(force));
    }

    public void resumeAll(final boolean force) {
        LOGGER.info("Resuming all listeners");
        listeners.values().forEach(listener -> listener.resume(force));
    }

    public boolean pauseListener(final String listenerId, final boolean force) {
        return Optional.ofNullable(listeners.get(listenerId))
                .map(it -> it.pause(force))
                .orElse(false);
    }

    public boolean resumeListener(final String listenerId, final boolean force) {
        return Optional.ofNullable(listeners.get(listenerId))
                .map(it -> it.resume(force))
                .orElse(false);
    }

    public ImmutableMap<String, ListenerStatus> getListenersStatus() {
        return listeners.values()
                .stream()
                .collect(ImmutableMap.toImmutableMap(DecoratedKafkaListener::getListenerId, DecoratedKafkaListener::getStatus));
    }
}
