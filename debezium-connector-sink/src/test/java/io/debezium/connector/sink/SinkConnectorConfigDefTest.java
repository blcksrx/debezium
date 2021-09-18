/*
 * Copyright Debezium Authors.
 *
 * Licensed under the Apache Software License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.debezium.connector.sink;

import static org.fest.assertions.Assertions.assertThat;

import java.util.Map;

import org.apache.kafka.common.config.ConfigDef;
import org.junit.Test;

public class SinkConnectorConfigDefTest {

    @Test
    public void allFieldsShouldHaveDescription() {
        final ConfigDef config = new SinkConnector().config();
        for (Map.Entry<String, ConfigDef.ConfigKey> configKey : config.configKeys().entrySet()) {
            assertThat(configKey.getValue().documentation)
                    .describedAs("Description of config key \"" + configKey.getKey() + "\"")
                    .isNotNull()
                    .isNotEmpty();
        }
    }
}
