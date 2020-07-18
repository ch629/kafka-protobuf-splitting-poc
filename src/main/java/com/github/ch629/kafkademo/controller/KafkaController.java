package com.github.ch629.kafkademo.controller;

import com.github.ch629.kafkademo.domain.proto.TestExtraProto;
import com.github.ch629.kafkademo.kafka.KafkaManager;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/kafka")
public class KafkaController {
    private final KafkaManager kafkaManager;
    private final KafkaTemplate<String, TestExtraProto> kafkaTemplate;

    public KafkaController(final KafkaManager kafkaManager, final KafkaTemplate<String, TestExtraProto> kafkaTemplate) {
        this.kafkaManager = kafkaManager;
        this.kafkaTemplate = kafkaTemplate;
    }

    @PostMapping("/send")
    public void postMessage() {
        kafkaTemplate.send("Test", TestExtraProto.newBuilder().setAbc("ABC").setName("NAME").build());
    }

    @PostMapping("/sendtest")
    public void postTestMessage() {
        kafkaTemplate.send("Test", TestExtraProto.newBuilder().setTest("TEST").setName("NAME").build());
    }

    @PutMapping("/resume")
    public void resume() {
        kafkaManager.resume();
    }

    @PutMapping("/pause")
    public void pause() {
        kafkaManager.pause();
    }
}
