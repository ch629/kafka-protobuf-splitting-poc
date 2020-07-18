package com.github.ch629.kafkademo.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.ch629.kafkademo.domain.mapper.ProtoCoreMapper;
import com.github.ch629.kafkademo.domain.proto.TestProto;
import com.github.ch629.kafkademo.domain.proto.TestProto.TestingCase;
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

    // TODO: These routes should take a core object, also they should really know inherently which version they need,
    //  not too sure how to handle that nicely
    private final ImmutableMap<TestingCase, BiConsumer<TestProto, Acknowledgment>> testRoutingMap =
            ImmutableMap.<TestingCase, BiConsumer<TestProto, Acknowledgment>>builder()
                    .put(TestingCase.ABC, this::handleAbc)
                    .put(TestingCase.TEST, this::handleTest)
                    .build();

    public TestRouter(final ObjectMapper objectMapper, final ProtoCoreMapper protoCoreMapper) {
        this.objectMapper = objectMapper;
        this.protoCoreMapper = protoCoreMapper;
    }

    @Override
    public void route(final TestProto testProto, final Acknowledgment ack) {
        fetchRoute(testProto).ifPresentOrElse(
                it -> it.accept(testProto, ack),
                ack::acknowledge
        );
    }

    private Optional<BiConsumer<TestProto, Acknowledgment>> fetchRoute(final TestProto testProto) {
        return Optional.ofNullable(testRoutingMap.get(testProto.getTestingCase()));
    }

    private void handleTest(final TestProto payload, final Acknowledgment ack) {
        LOGGER.info("Handle TEST: {}", writeValue(payload));
        ack.acknowledge();
    }

    private void handleAbc(final TestProto payload, final Acknowledgment ack) {
        LOGGER.info("Handle ABC: {}", writeValue(payload));
        ack.acknowledge();
    }

    private String writeValue(final TestProto payload) {
        return Try.of(() -> objectMapper.writeValueAsString(protoCoreMapper.mapTest(payload))).getOrElse("");
    }
}
