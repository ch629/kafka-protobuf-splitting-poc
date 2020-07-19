package com.github.ch629.kafkademo.kafka;

import com.github.ch629.kafkademo.domain.core.*;
import com.github.ch629.kafkademo.domain.mapper.ProtoCoreMapper;
import com.github.ch629.kafkademo.domain.proto.TestProto;
import com.github.ch629.kafkademo.kafka.routes.Route;
import com.google.common.collect.ImmutableMap;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class TestRouter implements MessageRouter<TestProto> {
    private final ProtoCoreMapper protoCoreMapper;

    private final ImmutableMap<Class<? extends Test>, Route<? extends Test>> testRoutingMap;

    public TestRouter(final ProtoCoreMapper protoCoreMapper, final List<Route<? extends Test>> routes) {
        this.protoCoreMapper = protoCoreMapper;

        testRoutingMap = routes.stream().collect(ImmutableMap.toImmutableMap(Route::getRouteClass, route -> route));
    }

    @Override
    public void route(final TestProto testProto, final Acknowledgment ack) {
        final var coreTest = Optional.ofNullable(protoCoreMapper.mapTest(testProto));

        coreTest.ifPresent(test -> call(test, ack));
    }

    private <T extends Test> void call(final Test test, final Acknowledgment ack) {
        fetchRoute(test.getClass())
                .ifPresent(route -> {
                    final Route<T> castedRoute = (Route<T>) route;
                    castedRoute.route(castedRoute.cast(test), ack);
                });
    }

    private <T> Optional<Route<?>> fetchRoute(final Class<? extends T> clazz) {
        return Optional.ofNullable(testRoutingMap.get(clazz));
    }
}
