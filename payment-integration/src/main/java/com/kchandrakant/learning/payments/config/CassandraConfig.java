package com.kchandrakant.learning.payments.config;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.cassandra.config.AbstractCassandraConfiguration;
import org.springframework.data.cassandra.config.CassandraClusterFactoryBean;
import org.springframework.data.cassandra.config.CassandraEntityClassScanner;
import org.springframework.data.cassandra.config.CassandraSessionFactoryBean;
import org.springframework.data.cassandra.config.SchemaAction;
import org.springframework.data.cassandra.core.convert.MappingCassandraConverter;
import org.springframework.data.cassandra.core.cql.keyspace.CreateKeyspaceSpecification;
import org.springframework.data.cassandra.core.cql.keyspace.KeyspaceOption;
import org.springframework.data.cassandra.core.mapping.CassandraMappingContext;
import org.springframework.data.cassandra.core.mapping.SimpleUserTypeResolver;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;

import com.datastax.driver.core.SocketOptions;

@Configuration
// @RefreshScope
@PropertySource(value = "classpath:application.yml")
@EnableCassandraRepositories(basePackages = "com.kchandrakant.learning.payments.repository")
public class CassandraConfig extends AbstractCassandraConfiguration {

    Logger logger = LoggerFactory.getLogger(CassandraConfig.class);

    @Value("${payments.cassandra.keyspace:coc_payments}")
    private String keySpace;
    @Value("${payments.cassandra.host:127.0.0.1}")
    private String host;
    @Value("${payments.cassandra.port:9042}")
    private int port;
    @Value("${payments.cassandra.username}")
    private String username;
    @Value("${payments.cassandra.password}")
    private String password;

    @Override
    protected String getKeyspaceName() {
        return keySpace;
    }

    @Override
    public CassandraClusterFactoryBean cluster() {
        SocketOptions socketOptions = new SocketOptions();
        socketOptions.setConnectTimeoutMillis(5000);
        socketOptions.setReadTimeoutMillis(5000);

        CassandraClusterFactoryBean cluster = new CassandraClusterFactoryBean();
        cluster.setKeyspaceCreations(getKeyspaceCreations());
        cluster.setSocketOptions(socketOptions);
        cluster.setContactPoints(host);
        cluster.setPort(port);
        cluster.setUsername(username);
        cluster.setPassword(password);
        return cluster;
    }

    @Override
    public CassandraMappingContext cassandraMapping() {
        CassandraMappingContext mappingContext = new CassandraMappingContext();
        mappingContext.setUserTypeResolver(new SimpleUserTypeResolver(cluster().getObject(), getKeyspaceName()));
        mappingContext.setInitialEntitySet(getInitialEntitySet());
        return mappingContext;
    }

    @Override
    protected Set<Class<?>> getInitialEntitySet() {
        try {
            return CassandraEntityClassScanner.scan(getEntityBasePackages());
        } catch (ClassNotFoundException e) {
            logger.error(e.getMessage());
        }
        return Collections.emptySet();
    }

    @Override
    protected List<CreateKeyspaceSpecification> getKeyspaceCreations() {
        final CreateKeyspaceSpecification specification = CreateKeyspaceSpecification.createKeyspace(getKeyspaceName())
            .ifNotExists()
            .with(KeyspaceOption.DURABLE_WRITES, true)
            .withSimpleReplication();
        return Arrays.asList(specification);
    }

    @Override
    public CassandraSessionFactoryBean session() {
        CassandraSessionFactoryBean session = new CassandraSessionFactoryBean();
        session.setCluster(cluster().getObject());
        session.setKeyspaceName(getKeyspaceName());
        session.setConverter(new MappingCassandraConverter(cassandraMapping()));
        session.setSchemaAction(SchemaAction.CREATE_IF_NOT_EXISTS);
        return session;
    }

    @Override
    public String[] getEntityBasePackages() {
        return new String[] { "com.kchandrakant.learning.payments.domain" };
    }
}