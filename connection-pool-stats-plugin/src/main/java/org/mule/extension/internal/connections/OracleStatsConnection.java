package org.mule.extension.internal.connections;


import oracle.ucp.admin.UniversalConnectionPoolMBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.management.*;
import java.lang.management.ManagementFactory;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class OracleStatsConnection extends DataSourceStatsConnection {
    Logger logger = LoggerFactory.getLogger(OracleStatsConnection.class);
    private String mbeanName;
    private final String DATASOURCE_FIELD_NAME = "poolName";
    private MBeanServerConnection mBeanServerConnection;

    public OracleStatsConnection(String mbeanName) {
        this.mbeanName = mbeanName;
       // logger.info("Initializing Oracle UCP Datasource tracking ....");
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
                           UniversalConnectionPoolMBean ucp =  JMX.newMBeanProxy(mBeanServerConnection, obj, UniversalConnectionPoolMBean.class);
                            ucp.purge();

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
        logger.info("Disposing Oracle UCP MBeanServer connection.. ");
        mBeanServerConnection = null;

    }


    @Override
    public List<String> findPools() {
        return findConnectionPools(mbeanName, mBeanServerConnection,DATASOURCE_FIELD_NAME);
    }

    @Override
    public List<Map<String, String>> getConnectionPoolStats(String poolName) {
        return getConnectionPoolStats(mbeanName, poolName, mBeanServerConnection);
    }


}
