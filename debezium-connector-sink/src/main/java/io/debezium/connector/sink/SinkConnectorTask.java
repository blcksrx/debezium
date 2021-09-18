/*
 * Copyright Debezium Authors.
 *
 * Licensed under the Apache Software License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.debezium.connector.sink;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.ReentrantLock;

import io.debezium.config.Configuration;
import org.apache.kafka.connect.errors.ConnectException;
import org.apache.kafka.connect.sink.SinkRecord;
import org.apache.kafka.connect.sink.SinkTask;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The main task executing streaming from sink connector.
 * Responsible for lifecycle management of the streaming code.
 *
 * @author Hossein Torabi
 *
 */
public class SinkConnectorTask extends SinkTask {
    private static enum State {
        RUNNING,
        STOPPED;
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(SinkConnectorTask.class);
    private static final String CONTEXT_NAME = "sink-connector-task";

    private final AtomicReference<State> state = new AtomicReference<State>(State.STOPPED);
    private final ReentrantLock stateLock = new ReentrantLock();
    private volatile Map<String, String> props;
    private volatile Configuration config;

    private final SessionFactory sessionFactory;

    @Override
    public String version() {
        return Module.version();
    }

    @Override
    public void start(Map<String, String> map) {
        if (context == null) {
            throw new ConnectException("Unexpected null context");
        }

        stateLock.lock();

        try {
            if (!state.compareAndSet(State.STOPPED, State.RUNNING)) {
                LOGGER.info("Connector has already been started");
                return;
            }

            props = map;
            config = Configuration.from(props);
            if (!config.validateAndRecord(SinkConnectorConfig.ALL_FIELDS, LOGGER::error)) {
                throw new ConnectException("Error configuring an instance of " + getClass().getSimpleName() + "; check the logs for details");
            }

            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("Starting {} with configuration:", getClass().getSimpleName());
                config.withMaskedPasswords().forEach((propName, propValue) -> {
                    LOGGER.info("   {} = {}", propName, propValue);
                });
            }
        }
        finally {
            stateLock.unlock();
        }
    }

    @Override
    public void put(Collection<SinkRecord> collection) {

    }

    @Override
    public void stop() {

    }

}
