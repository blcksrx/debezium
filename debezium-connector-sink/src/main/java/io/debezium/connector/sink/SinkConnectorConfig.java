/*
 * Copyright Debezium Authors.
 *
 * Licensed under the Apache Software License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.debezium.connector.sink;

import org.apache.kafka.common.config.ConfigDef;

import io.debezium.config.ConfigDefinition;
import io.debezium.config.Configuration;
import io.debezium.config.Field;

/**
 * The configuration properties.
 *
 * @author Hossein Torabi
 */
public class SinkConnectorConfig {

    public static final String DATABASE_CONFIG_PREFIX = "connection.";
    public static final String URL = "url";
    public static final String USER = "username";
    public static final String PASSWORD = "password";

    public static final Field CONNECTION_URL = Field.create(DATABASE_CONFIG_PREFIX + URL)
            .withDisplayName("Hostname")
            .withType(ConfigDef.Type.STRING)
            .withGroup(Field.createGroupEntry(Field.Group.CONNECTION, 2))
            .withWidth(ConfigDef.Width.LONG)
            .withImportance(ConfigDef.Importance.HIGH)
            .required()
            .withDescription("Valid JDBC URL");

    public static final Field CONNECTION_USER = Field.create(DATABASE_CONFIG_PREFIX +  USER)
            .withDisplayName("User")
            .withType(ConfigDef.Type.STRING)
            .withGroup(Field.createGroupEntry(Field.Group.CONNECTION, 4))
            .withWidth(ConfigDef.Width.SHORT)
            .withImportance(ConfigDef.Importance.HIGH)
            .required()
            .withDescription("Name of the database user to be used when connecting to the connection.");

    public static final Field CONNECTION_PASSWORD = Field.create(DATABASE_CONFIG_PREFIX + PASSWORD)
            .withDisplayName("Password")
            .withType(ConfigDef.Type.PASSWORD)
            .withGroup(Field.createGroupEntry(Field.Group.CONNECTION, 5))
            .withWidth(ConfigDef.Width.SHORT)
            .withImportance(ConfigDef.Importance.HIGH)
            .required()
            .withDescription("Password of the database user to be used when connecting to the connection.");

    protected static final ConfigDefinition CONFIG_DEFINITION = ConfigDefinition.editor()
            .connector(
                    CONNECTION_URL,
                    CONNECTION_USER,
                    CONNECTION_PASSWORD)
            .create();

    private final Configuration config;

    protected SinkConnectorConfig(Configuration config) {
        this.config = config;
    }

    protected static ConfigDef configDef() {
        return CONFIG_DEFINITION.configDef();
    }

    /**
     * The set of {@link Field}s defined as part of this configuration.
     */
    public static Field.Set ALL_FIELDS = Field.setOf(CONFIG_DEFINITION.all());

    public String getContextName() {
        return Module.contextName();
    }

    public String getConnectorName() {
        return Module.name();
    }

}
