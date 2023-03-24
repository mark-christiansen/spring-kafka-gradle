package com.mycompany.kafka.streams.accountcontact;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {
        "com.mycompany.kafka.streams.spring",
        "com.mycompany.kafka.streams.accountcontact"}
)
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}