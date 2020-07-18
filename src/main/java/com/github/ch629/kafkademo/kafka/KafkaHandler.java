package com.github.ch629.kafkademo.kafka;

import com.github.ch629.kafkademo.domain.proto.TestProto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
public class KafkaHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaHandler.class);
    private final MessageRouter<TestProto> testMessageRouter;

    public KafkaHandler(final MessageRouter<TestProto> testMessageRouter) {
        this.testMessageRouter = testMessageRouter;
    }

    @KafkaListener(id = "listener", groupId = "group", topics = "Test")
    public void testTopicListener(@Payload final TestProto payload, final Acknowledgment acknowledgment) {
        LOGGER.info("Received: {}", payload);
        testMessageRouter.route(payload, acknowledgment);
    }
}
