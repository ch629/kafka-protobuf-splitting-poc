package com.github.ch629.kafkademo.kafka.routes;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.ch629.kafkademo.domain.core.ImmutableTestTest;
import com.github.ch629.kafkademo.domain.core.TestTest;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Component
public class TestRoute implements Route<TestTest> {
    private static final Logger LOGGER = LoggerFactory.getLogger(TestRoute.class);

    private final ObjectMapper objectMapper;

    public TestRoute(final ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public Class<ImmutableTestTest> getRouteClass() {
        return ImmutableTestTest.class;
    }

    @Override
    public void route(final TestTest payload, final Acknowledgment ack) {
        LOGGER.info("Handle TEST: {}", Try.of(() -> objectMapper.writeValueAsString(payload)).getOrElse(""));
        ack.acknowledge();
    }
}
