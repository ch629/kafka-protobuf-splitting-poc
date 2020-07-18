package com.github.ch629.kafkademo.domain.mapper;

import com.github.ch629.kafkademo.domain.core.*;
import com.github.ch629.kafkademo.domain.core.ImmutableAbcTest;
import com.github.ch629.kafkademo.domain.core.ImmutableTest;
import com.github.ch629.kafkademo.domain.core.ImmutableTestTest;
import com.github.ch629.kafkademo.domain.proto.TestProto;
import com.github.ch629.kafkademo.domain.proto.TestProto.TestingCase;
import com.google.common.collect.ImmutableMap;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.function.Function;

@Component
public class ProtoCoreMapper {
    private final ImmutableMap<TestingCase, Function<TestProto, Test>> mappings =
            ImmutableMap.<TestingCase, Function<TestProto, Test>>builder()
                    .put(TestingCase.ABC, this::mapAbcTest)
                    .put(TestingCase.TEST, this::mapTestTest)
                    .build();

    public AbcTest mapAbcTest(final TestProto testProto) {
        return ImmutableAbcTest.builder()
                .from(mapTestBuilder(testProto))
                .abc(testProto.getAbc())
                .build();
    }

    public TestTest mapTestTest(final TestProto testProto) {
        return ImmutableTestTest.builder()
                .from(mapTestBuilder(testProto))
                .test(testProto.getTest())
                .build();
    }

    private ImmutableTest mapTestBuilder(final TestProto testProto) {
        return ImmutableTest.builder().name(testProto.getName()).build();
    }

    public Test mapTest(final TestProto testProto) {
        return Optional.ofNullable(mappings.get(testProto.getTestingCase())).map(it -> it.apply(testProto)).orElse(null);
    }
}
