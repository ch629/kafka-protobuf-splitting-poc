package com.github.ch629.kafkademo.controller;

import com.github.ch629.kafkademo.domain.core.kafka.ListenerStatus;
import com.github.ch629.kafkademo.domain.proto.TestExtraProto;
import com.github.ch629.kafkademo.kafka.KafkaManager;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

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
    public ResponseEntity<Void> resume() {
        kafkaManager.resumeAll(true);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/pause")
    public ResponseEntity<Void> pause() {
        kafkaManager.pauseAll(true);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{listenerName}/pause")
    public ResponseEntity<Void> pause(@PathVariable final String listenerName) {
        return kafkaManager.pauseListener(listenerName, true) ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    @PutMapping("/{listenerName}/resume")
    public ResponseEntity<Void> resume(@PathVariable final String listenerName) {
        return kafkaManager.resumeListener(listenerName, true) ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<Map<String, ListenerStatus>> fetchAllStatus() {
        return ResponseEntity.ok(kafkaManager.getListenersStatus());
    }
}
