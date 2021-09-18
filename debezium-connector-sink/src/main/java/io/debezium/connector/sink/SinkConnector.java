/*
 * Copyright Debezium Authors.
 *
 * Licensed under the Apache Software License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.debezium.connector.sink;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.connect.connector.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.debezium.annotation.Immutable;

/**
 *  A Kafka Connect sink connector that creates {@link SinkConnectorTask tasks}  that read data change events from Kafka
 *  (that Debezium produced it) and put it to the corresponding database
 *
 *   <h2>Configuration</h2>
 *   <p>This connector is configured with the set of properties described in {@link SinkConnectorConfig}.</p>
 *
 *   @author Hossein Torabi
 */
public class SinkConnector extends org.apache.kafka.connect.sink.SinkConnector {
    private static final Logger LOGGER = LoggerFactory.getLogger(SinkConnector.class);

    public SinkConnector() {
    }

    @Immutable
    private Map<String, String> properties;

    @Override
    public String version() {
        return Module.version();
    }

    @Override
    public void start(Map<String, String> props) {
        this.properties = Collections.unmodifiableMap(new HashMap<>(props));
    }

    @Override
    public Class<? extends Task> taskClass() {
        return SinkConnectorTask.class;
    }

    @Override
    public List<Map<String, String>> taskConfigs(int maxTasks) {
        LOGGER.info("Setting task configurations for {} workers.", maxTasks);
        final List<Map<String, String>> configs = new ArrayList<>(maxTasks);
        for (int i = 0; i < maxTasks; ++i) {
            configs.add(properties);
        }
        return configs;
    }

    @Override
    public void stop() {

    }

    @Override
    public ConfigDef config() {
        return SinkConnectorConfig.configDef();
    }

}
