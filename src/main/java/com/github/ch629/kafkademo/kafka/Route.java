package com.github.ch629.kafkademo.kafka;

import com.github.ch629.kafkademo.domain.core.Test;
import org.springframework.kafka.support.Acknowledgment;

@FunctionalInterface
public interface Route<T extends Test> {
    default T convert(final Test test) {
        return (T) test;
    }

    void route(final T payload, final Acknowledgment acknowledgment);
}
