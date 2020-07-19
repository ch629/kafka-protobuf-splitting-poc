package com.github.ch629.kafkademo.kafka.routes;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.ch629.kafkademo.domain.core.AbcTest;
import com.github.ch629.kafkademo.domain.core.ImmutableAbcTest;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Component
public class AbcRoute implements Route<AbcTest> {
    private static final Logger LOGGER = LoggerFactory.getLogger(AbcRoute.class);

    private final ObjectMapper objectMapper;

    public AbcRoute(final ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public Class<ImmutableAbcTest> getRouteClass() {
        return ImmutableAbcTest.class;
    }

    @Override
    public void route(final AbcTest payload, final Acknowledgment ack) {
        LOGGER.info("Handle ABC: {}", Try.of(() -> objectMapper.writeValueAsString(payload)).getOrElse(""));

        ack.acknowledge();
    }
}
