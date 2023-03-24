package com.mycompany.kafka.streams.spring;

import com.mycompany.kafka.streams.topology.TopologyBuilder;

import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.streams.Topology;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

@Configuration
public class StreamFactory {

    private static final String KAFKA_STREAMS_PROPS_PREFIX = "kafka.streams";
    private static final String SCHEMA_REGISTRY_URL = "schema.registry.url";
    private static final String SCHEMA_CACHE_CAPACITY = "schema.cache.capacity";

    @Bean(name = "applicationProperties")
    @ConfigurationProperties(prefix = "application")
    public Properties applicationProperties() {
        return new Properties();
    }

    @Bean
    @ConfigurationProperties(prefix = "kafka.streams")
    public Properties streamProperties() {
        return new Properties();
    }

    @Bean
    @ConfigurationProperties(prefix = "kafka.producer")
    public Properties producerProperties() {
        return new Properties();
    }

    @Bean
    public Producer<Long, GenericRecord> producer() {
        return new KafkaProducer<>(producerProperties());
    }

    @Bean
    public Topology topology(TopologyBuilder topologyBuilder) {
        return topologyBuilder.build(streamProperties());
    }
}
