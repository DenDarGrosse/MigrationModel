package com.testtask.migrationmodel.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.cassandra.config.AbstractCassandraConfiguration;

@Configuration
public class CassandraConfig extends AbstractCassandraConfiguration {
    @Value("${cassandra.contactPoints}")
    private String contactPoints;
    @Value("${cassandra.keyspaceName}")
    private String keyspaceName;

    public String getContactPoints() {
        return contactPoints;
    }

    public String getKeyspaceName() {
        return keyspaceName;
    }

    public String[] getEntityBasePackages() {
        return new String[] { "com.testtask.migrationmodel.entity" };
    }
}