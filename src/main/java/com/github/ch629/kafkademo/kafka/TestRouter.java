package com.github.ch629.kafkademo.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.ch629.kafkademo.domain.core.*;
import com.github.ch629.kafkademo.domain.core.ImmutableAbcTest;
import com.github.ch629.kafkademo.domain.core.ImmutableTestTest;
import com.github.ch629.kafkademo.domain.mapper.ProtoCoreMapper;
import com.github.ch629.kafkademo.domain.proto.TestProto;
import com.google.common.collect.ImmutableMap;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.function.BiConsumer;

@Component
public class TestRouter implements MessageRouter<TestProto> {
    private static final Logger LOGGER = LoggerFactory.getLogger(TestRouter.class);

    private final ObjectMapper objectMapper;
    private final ProtoCoreMapper protoCoreMapper;

    private final ImmutableMap<Class<? extends Test>, BiConsumer<Test, Acknowledgment>> testRoutingMap =
            ImmutableMap.<Class<? extends Test>, BiConsumer<Test, Acknowledgment>>builder()
                    .put(ImmutableAbcTest.class, this::handleAbc)
                    .put(ImmutableTestTest.class, this::handleTest)
                    .build();

    public TestRouter(final ObjectMapper objectMapper, final ProtoCoreMapper protoCoreMapper) {
        this.objectMapper = objectMapper;
        this.protoCoreMapper = protoCoreMapper;
    }

    @Override
    public void route(final TestProto testProto, final Acknowledgment ack) {
        final var coreTest = Optional.ofNullable(protoCoreMapper.mapTest(testProto));

        coreTest.ifPresentOrElse(test -> fetchRoute(test.getClass())
                        .ifPresentOrElse(it -> it.accept(test, ack),
                                ack::acknowledge),
                ack::acknowledge);
    }

    private <T> Optional<BiConsumer<Test, Acknowledgment>> fetchRoute(final Class<? extends T> clazz) {
        LOGGER.info("fetchRoute: {}", clazz.getSimpleName());
        return Optional.ofNullable(testRoutingMap.get(clazz));
    }

    private void handleTest(final Test payload, final Acknowledgment ack) {
        LOGGER.info("Handle TEST: {}", writeValue(payload));
        ack.acknowledge();
    }

    private void handleAbc(final Test payload, final Acknowledgment ack) {
        LOGGER.info("Handle ABC: {}", writeValue(payload));
        ack.acknowledge();
    }

    private String writeValue(final Test payload) {
        return Try.of(() -> objectMapper.writeValueAsString(payload)).getOrElse("");
    }
}
