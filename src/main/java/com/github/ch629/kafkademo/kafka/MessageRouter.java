package com.github.ch629.kafkademo.kafka;

import org.springframework.kafka.support.Acknowledgment;

public interface MessageRouter<T> {
    /**
     * @return if the route exists
     */
    void route(final T payload, final Acknowledgment acknowledgment);
}
