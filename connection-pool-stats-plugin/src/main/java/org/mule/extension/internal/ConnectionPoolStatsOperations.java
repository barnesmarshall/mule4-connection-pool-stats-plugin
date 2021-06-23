package org.mule.extension.internal;

import static org.mule.runtime.extension.api.annotation.param.MediaType.ANY;

import org.mule.extension.internal.connections.DataSourceStatsConnection;
import org.mule.runtime.api.meta.model.operation.ExecutionType;
import org.mule.runtime.extension.api.annotation.execution.Execution;
import org.mule.runtime.extension.api.annotation.param.MediaType;
import org.mule.runtime.extension.api.annotation.param.Connection;
import org.mule.runtime.extension.api.annotation.param.display.DisplayName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

public class ConnectionPoolStatsOperations {
    private static Logger logger = LoggerFactory.getLogger(ConnectionPoolStatsOperations.class);

    @MediaType(value = ANY)
    @Execution(value = ExecutionType.CPU_INTENSIVE)
    public List<String> getConnectionPools(@Connection DataSourceStatsConnection connection) {
        return connection.findPools();
    }

    @MediaType(value = ANY)
    @Execution(value = ExecutionType.CPU_INTENSIVE)
    @DisplayName("Get Connection Pool Stats")
    public List<Map<String, String>> getConnectionPoolStats(@Connection DataSourceStatsConnection connection, String poolName) {
        return connection.getConnectionPoolStats(poolName);
    }


    @MediaType(value = ANY)
    @Execution(value = ExecutionType.CPU_INTENSIVE)
    @DisplayName(value = "Reset Connection Pool")
    public void resetPool(@Connection DataSourceStatsConnection connection, String poolName) {
        connection.resetPool(poolName);

    }
}
