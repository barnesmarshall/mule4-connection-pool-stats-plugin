package org.mule.extension.internal.connections;

import com.mchange.v2.c3p0.PooledDataSource;
import org.mule.runtime.extension.api.annotation.param.Optional;
import org.mule.runtime.extension.api.annotation.param.Parameter;
import org.mule.runtime.extension.api.annotation.param.display.DisplayName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.management.*;
import java.lang.management.ManagementFactory;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class C3P0StatsConnection extends DataSourceStatsConnection {
    Logger logger = LoggerFactory.getLogger(C3P0StatsConnection.class);

    private String mbeanName;
    private final String DATASOURCE_FIELD_NAME = "dataSourceName";
    private MBeanServerConnection mBeanServerConnection;

    public C3P0StatsConnection(String mbeanName) {
        this.mbeanName = mbeanName;
       // logger.info("Initializing C3P0 Datasource tracking ....");
        this.mBeanServerConnection = ManagementFactory.getPlatformMBeanServer();
    }

    @Override
    public void resetPool(String poolName) {
        try {
            Set<ObjectName> objects = mBeanServerConnection.queryNames(new ObjectName(mbeanName), null);
            for (ObjectName obj : objects) {
                MBeanInfo info = mBeanServerConnection.getMBeanInfo(obj);
                MBeanAttributeInfo[] attr = info.getAttributes();
                for (int i = 0; i < attr.length; i++) {
                    String name = attr[i].getName();
                    if (name.equalsIgnoreCase(DATASOURCE_FIELD_NAME)) {
                        String value = String.valueOf(mBeanServerConnection.getAttribute(obj, name));
                        if (value.equalsIgnoreCase(poolName)) {
                            PooledDataSource pooledDataSource = JMX.newMBeanProxy(mBeanServerConnection, obj, PooledDataSource.class);
                            pooledDataSource.hardReset();
                        }
                    }
                }

            }
        } catch (Exception e) {
            logger.error("Error in resetting pool {}", poolName, e);

        }

    }

    @Override
    public void invalidate() {

    }


    @Override
    public List<String> findPools() {
        return findConnectionPools(mbeanName, mBeanServerConnection);
    }

    @Override
    public List<Map<String, String>> getConnectionPoolStats(String poolName) {
        return getConnectionPoolStats(mbeanName, poolName, mBeanServerConnection);
    }
}
