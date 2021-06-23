package org.mule.extension.internal.connections;

import org.mule.runtime.api.connection.ConnectionException;
import org.mule.runtime.api.meta.ExternalLibraryType;
import org.mule.runtime.extension.api.annotation.Alias;
import org.mule.runtime.extension.api.annotation.ExternalLib;
import org.mule.runtime.api.connection.ConnectionValidationResult;
import org.mule.runtime.api.connection.ConnectionProvider;
import org.mule.runtime.extension.api.annotation.param.Optional;
import org.mule.runtime.extension.api.annotation.param.Parameter;
import org.mule.runtime.extension.api.annotation.param.display.DisplayName;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@ExternalLib(name = "C3PO Datasource Jar",
        description = "Jar to access MBean operations exposed by C3P0 API",
        nameRegexpMatcher = "(.*)\\.jar",
        type = ExternalLibraryType.JAR,
        coordinates = "com.mchange:c3p0:0.9.5.5")
@DisplayName("C3P0 DataSource")
@Alias("C3P0")
public class C3P0MbeanConnectionProvider implements ConnectionProvider<DataSourceStatsConnection> {
    private final Logger LOGGER = LoggerFactory.getLogger(C3P0MbeanConnectionProvider.class);

    @Parameter
    @DisplayName(value = "Search Pattern")
    @Optional(defaultValue = "com.mchange.v2.c3p0:type=PooledDataSource*,*")
    private  String mbeanName;


    @Override
    public DataSourceStatsConnection connect() throws ConnectionException {
        return new C3P0StatsConnection(mbeanName);
    }

    @Override
    public void disconnect(DataSourceStatsConnection connection) {
        try {
            connection.invalidate();
        } catch (Exception e) {
            LOGGER.error("Error while disconnecting  : " + e.getMessage(), e);
        }
    }

    @Override
    public ConnectionValidationResult validate(DataSourceStatsConnection connection) {
        return ConnectionValidationResult.success();
    }


}