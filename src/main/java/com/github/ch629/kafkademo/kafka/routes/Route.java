package com.github.ch629.kafkademo.kafka.routes;

import com.github.ch629.kafkademo.domain.core.Test;
import org.springframework.kafka.support.Acknowledgment;

public interface Route<T extends Test> {
    default T cast(final Test test) {
        return (T) test;
    }

    Class<? extends T> getRouteClass();

    void route(final T payload, final Acknowledgment ack);
}
