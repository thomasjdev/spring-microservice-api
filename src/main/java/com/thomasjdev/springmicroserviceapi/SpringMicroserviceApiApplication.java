package com.thomasjdev.springmicroserviceapi;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class SpringMicroserviceApiApplication {

    public static void main(String[] args) {
        log.info("Starting Spring Server");
        SpringApplication.run(SpringMicroserviceApiApplication.class, args);
    }

}

