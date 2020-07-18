package com.github.ch629.kafkademo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.github.ch629.kafkademo")
public class KafkaDemoApplication {

    public static void main(final String[] args) {
        SpringApplication.run(KafkaDemoApplication.class, args);
    }
}
