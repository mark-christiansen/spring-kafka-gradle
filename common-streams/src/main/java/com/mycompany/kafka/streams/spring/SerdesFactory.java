package com.mycompany.kafka.streams.spring;

import com.mycompany.kafka.streams.Serdes;
import io.confluent.kafka.schemaregistry.client.CachedSchemaRegistryClient;
import io.confluent.kafka.schemaregistry.client.SchemaRegistryClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Configuration
public class SerdesFactory {

    private static final String KAFKA_STREAMS_PROPS_PREFIX = "kafka.streams";
    private static final String SCHEMA_REGISTRY_URL = "schema.registry.url";
    private static final String SCHEMA_CACHE_CAPACITY = "schema.cache.capacity";

    @Qualifier("streamProperties")
    @Autowired
    private Properties streamProps;

    @Bean
    public SchemaRegistryClient schemaRegistryClient() {

        // pull out schema registry properties from kafka properties to pass to schema registry client
        Map<String, Object> schemaProps = new HashMap<>();
        for (Map.Entry<Object, Object> entry : streamProps.entrySet()) {
            String propertyName = (String) entry.getKey();
            if (propertyName.startsWith("schema.registry.") || propertyName.startsWith("basic.auth.")) {
                schemaProps.put(propertyName, entry.getValue());
            }
        }

        return new CachedSchemaRegistryClient((String) streamProps.get(SCHEMA_REGISTRY_URL),
                Integer.parseInt((String) streamProps.get(SCHEMA_CACHE_CAPACITY)), schemaProps);
    }

    @Bean
    public Serdes serdes(@Qualifier("streamProperties") Properties streamProps) {
        return new Serdes(schemaRegistryClient(), streamProps);
    }
}
