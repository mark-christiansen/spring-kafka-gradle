package com.mycompany.kafka.streams.contactaddress;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {
        "com.mycompany.kafka.streams.spring",
        "com.mycompany.kafka.streams.contactaddress"}
)
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}