package com.github.ch629.kafkademo.serializers;

import com.github.ch629.kafkademo.domain.proto.TestProto;
import io.vavr.control.Try;
import org.apache.kafka.common.serialization.Deserializer;

public class KafkaProtobufDeserializer implements Deserializer<TestProto> {
    @Override
    public TestProto deserialize(final String topic, final byte[] data) {
        return Try.of(() -> TestProto.parseFrom(data)).getOrElse((TestProto) null);
    }
}
