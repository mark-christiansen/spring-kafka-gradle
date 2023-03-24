package com.mycompany.kafka.streams.spring;

import org.apache.kafka.streams.*;
import org.apache.kafka.streams.errors.StreamsUncaughtExceptionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.time.Duration;
import java.util.Properties;

import static java.lang.String.format;

@Component
public class StreamsLifecycle {

    private static final Logger log = LoggerFactory.getLogger(StreamsLifecycle.class);
    private static final String STATE_STORE_CLEANUP = "state.store.cleanup";
    private final String applicationId;
    private final Boolean stateStoreCleanup;
    private final Topology topology;
    private final KafkaStreams streams;
    private final ApplicationContext applicationContext;

    public StreamsLifecycle(Topology topology,
                            @Qualifier("applicationProperties") Properties applicationProperties,
                            @Qualifier("streamProperties") Properties streamProperties,
                            ApplicationContext applicationContext) {
        this.topology = topology;
        this.applicationId = streamProperties.getProperty(StreamsConfig.APPLICATION_ID_CONFIG);
        this.stateStoreCleanup = Boolean.parseBoolean(applicationProperties.getProperty(STATE_STORE_CLEANUP));
        streamProperties.put(StreamsConfig.APPLICATION_ID_CONFIG, applicationId);
        streamProperties.put(StreamsConfig.CLIENT_ID_CONFIG, applicationId);
        this.applicationContext = applicationContext;

        //KafkaClientSupplier supplier = new TracingKafkaClientSupplier(tracer);
        //this.streams = new KafkaStreams(topology, streamProperties, supplier);
        this.streams = new KafkaStreams(topology, streamProperties);
    }

    @PostConstruct
    private void start() {

        final TopologyDescription description = topology.describe();
        log.info("=======================================================================================");
        log.info("Topology: {}", description);
        log.info("=======================================================================================");

        log.info("Starting Stream {}", applicationId);
        if (streams != null) {

            streams.setUncaughtExceptionHandler(e -> {
                log.error(format("Stopping the application %s due to unhandled exception", applicationId), e);
                SpringApplication.exit(applicationContext, () -> 1);
                return StreamsUncaughtExceptionHandler.StreamThreadExceptionResponse.SHUTDOWN_APPLICATION;
            });

            streams.setStateListener((newState, oldState) -> {
                if (newState == KafkaStreams.State.ERROR) {
                    throw new RuntimeException("Kafka Streams went into an ERROR state");
                }
            });

            if (stateStoreCleanup) {
                streams.cleanUp();
            }

            streams.start();
        }
    }

    @PreDestroy
    private void destroy() {
        log.warn("Closing Kafka Streams application {}", applicationId);
        if (streams != null) {
            streams.close(Duration.ofSeconds(5));
            log.info("Closed Kafka Streams application {}", applicationId);
        }
    }

    public Boolean isHealthy() {
        return streams.state().isRunningOrRebalancing();
    }

    public String topologyDescription() {
        return topology.describe().toString();
    }
}
