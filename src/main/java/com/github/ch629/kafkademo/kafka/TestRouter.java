package com.github.ch629.kafkademo.kafka;

import com.github.ch629.kafkademo.domain.core.*;
import com.github.ch629.kafkademo.domain.core.ImmutableAbcTest;
import com.github.ch629.kafkademo.domain.core.ImmutableTestTest;
import com.github.ch629.kafkademo.domain.mapper.ProtoCoreMapper;
import com.github.ch629.kafkademo.domain.proto.TestProto;
import com.google.common.collect.ImmutableMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class TestRouter implements MessageRouter<TestProto> {
    private static final Logger LOGGER = LoggerFactory.getLogger(TestRouter.class);

    private final ProtoCoreMapper protoCoreMapper;

    private final ImmutableMap<Class<? extends Test>, Route<?>> testRoutingMap;

    public TestRouter(final ProtoCoreMapper protoCoreMapper, Route<AbcTest> abcTestRoute, Route<TestTest> testTestRoute) {
        this.protoCoreMapper = protoCoreMapper;

        testRoutingMap = ImmutableMap.<Class<? extends Test>, Route<?>>builder()
                .put(ImmutableAbcTest.class, abcTestRoute)
                .put(ImmutableTestTest.class, testTestRoute)
                .build();
    }

    @Override
    public void route(final TestProto testProto, final Acknowledgment ack) {
        final var coreTest = Optional.ofNullable(protoCoreMapper.mapTest(testProto));

        coreTest.ifPresent(test -> call(test.getClass(), test, ack));
    }

    private <T extends Test> void call(final Class<? extends T> clazz, final Test test, final Acknowledgment acknowledgment) {
        fetchRoute(clazz)
                .ifPresent(route -> {
                    final Route<T> castedRoute = (Route<T>) route;
                    castedRoute.route(castedRoute.convert(test), acknowledgment);
                });
    }

    private <T> Optional<Route<?>> fetchRoute(final Class<? extends T> clazz) {
        LOGGER.info("fetchRoute: {}", clazz.getSimpleName());
        return Optional.ofNullable(testRoutingMap.get(clazz));
    }
}
