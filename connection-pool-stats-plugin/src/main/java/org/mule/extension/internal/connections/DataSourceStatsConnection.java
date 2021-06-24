package org.mule.extension.internal.connections;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.management.MBeanAttributeInfo;
import javax.management.MBeanInfo;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import java.util.*;

/**
 * This class represents an extension connection just as example (there is no real connection with anything here c:).
 */
public abstract class DataSourceStatsConnection {
    private final Logger logger = LoggerFactory.getLogger(DataSourceStatsConnection.class);

    public abstract void resetPool(String poolName);

    public abstract void invalidate();

    public abstract List<String> findPools();

    public abstract List<Map<String, String>> getConnectionPoolStats(String poolName);


    public List<String> findConnectionPools(String searchPattern, MBeanServerConnection mbeanServer, String dataSourceField) {
        if (logger.isDebugEnabled()) {
            logger.debug("Finding connection with search Pattern [{}]", searchPattern);
        }
        List<String> pools = new ArrayList<>();
        try {
            Set<ObjectName> objectNames = mbeanServer.queryNames(new ObjectName(searchPattern), null);
            for (ObjectName obj : objectNames) {
                pools.add(String.valueOf(mbeanServer.getAttribute(obj,dataSourceField)));
            }
            return pools;

        } catch (Exception e) {
            logger.error("Error in finding connection Pools  ", e);
        }
        return pools;
    }

    public List<Map<String, String>> getConnectionPoolStats(String searchPattern, String poolName, MBeanServerConnection mBeanServer) {
        List<Map<String, String>> stats = new ArrayList<>();
        try {
            logger.info("Search pattern {} ", searchPattern);
            Set<ObjectName> objects = mBeanServer.queryNames(new ObjectName(searchPattern), null);
            for (ObjectName obj : objects) {
                Map<String, String> infoMap = new HashMap<String, String>();
                MBeanInfo info = mBeanServer.getMBeanInfo(obj);
                MBeanAttributeInfo[] attr = info.getAttributes();
                for (int i = 0; i < attr.length; i++) {
                    String name = attr[i].getName();
                    if (attr[i].isReadable()) {
                        readAttributesValue(mBeanServer, obj, infoMap, name);
                    }
                }

                stats.add(infoMap);
            }
        } catch (Exception e) {
            logger.error("Error in getting ConnectionPool stats  ", e);

        }
        return stats;

    }


    private void readAttributesValue(MBeanServerConnection mBeanServer, ObjectName obj, Map<String, String> infoMap, String name) {
        try {
            Object value = mBeanServer.getAttribute(obj, name);
            infoMap.put(name, String.valueOf(value));
        } catch (Exception e) {
            logger.warn("Error in reading config {}, error {}", name, e);
        }
    }

}
