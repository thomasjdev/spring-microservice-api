package com.thomasjdev.springmicroserviceapi;

import io.micrometer.core.annotation.Timed;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Set;

@Slf4j
@SpringBootApplication
public class SpringMicroserviceApiApplication {

    public static void main(String[] args) {
        log.info("Starting Spring Server");
        SpringApplication.run(SpringMicroserviceApiApplication.class, args);
    }

}

@RestController
class AvailabilityController {

    @Value(value = "${kafka.topicName}")
    private String topicName;

    private boolean validate(String console) {
        return StringUtils.hasText(console) &&
                Set.of("ps5", "ps4", "switch", "xbox").contains(console);
    }

    @GetMapping("/availability/{console}")
    Map<String, Object> getAvailability(@PathVariable String console) {
        sendMessage(console);
        return Map.of("console", console, "availability", checkAvailabliity(console));
    }

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;
    private void sendMessage(String message) {
        
        kafkaTemplate.send(topicName, message);
    }

    private boolean checkAvailabliity(String console) {
        Assert.state(validate(console), () -> "the console specified, " + console + ", is not valid.");
        return switch (console) {
            case "ps5" -> throw new RuntimeException("Service exception");
            case "xbox" -> true;
            default -> false;
        };
    }
}
