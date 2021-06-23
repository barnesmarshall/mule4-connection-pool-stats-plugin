package org.mule.extension.internal.connections;

import org.mule.runtime.api.connection.*;
import org.mule.runtime.api.meta.ExternalLibraryType;
import org.mule.runtime.extension.api.annotation.Alias;
import org.mule.runtime.extension.api.annotation.ExternalLib;
import org.mule.runtime.extension.api.annotation.param.Optional;
import org.mule.runtime.extension.api.annotation.param.Parameter;
import org.mule.runtime.extension.api.annotation.param.display.DisplayName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;


@ExternalLib(name = "UCP Datasource Jar",
        description = "Jar to access MBean operations exposed by UCP API",
        nameRegexpMatcher = "(.*)\\.jar",
        type = ExternalLibraryType.JAR,
        coordinates = "com.oracle.jdbc:ucp:12.2.0.1")
@DisplayName("Oracle UCP DataSource")
@Alias("OracleUCPDataSource")
public class OracleUCPConnectionProvider implements ConnectionProvider<DataSourceStatsConnection> {
    private final Logger LOGGER = LoggerFactory.getLogger(OracleUCPConnectionProvider.class);

    @Parameter
    @DisplayName(value = "Search Pattern")
    @Optional(defaultValue = "oracle.ucp.admin.UniversalConnectionPoolMBean:*")
    private  String mbeanName;
    @Override
    public DataSourceStatsConnection connect() throws ConnectionException {
        return new OracleStatsConnection(mbeanName);
    }
    @Override
    public void disconnect(DataSourceStatsConnection connection) {
        try {
            connection.invalidate();
        } catch (Exception e) {
            LOGGER.error("Error while disconnecting " + e.getMessage(), e);
        }
    }

    @Override
    public ConnectionValidationResult validate(DataSourceStatsConnection connection) {
        List<String> pools = connection.findPools();

        return ConnectionValidationResult.success();
    }
}
