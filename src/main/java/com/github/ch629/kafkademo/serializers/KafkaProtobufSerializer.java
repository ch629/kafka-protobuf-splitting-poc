package com.github.ch629.kafkademo.serializers;

import com.google.protobuf.MessageLite;
import org.apache.kafka.common.serialization.Serializer;

public class KafkaProtobufSerializer implements Serializer<MessageLite> {
    @Override
    public byte[] serialize(final String topic, final MessageLite data) {
        return data.toByteArray();
    }
}
