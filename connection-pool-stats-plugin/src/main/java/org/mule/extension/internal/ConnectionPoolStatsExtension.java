package org.mule.extension.internal;

import org.mule.extension.internal.connections.C3P0MbeanConnectionProvider;
import org.mule.extension.internal.connections.OracleUCPConnectionProvider;
import org.mule.runtime.extension.api.annotation.Extension;
import org.mule.runtime.extension.api.annotation.Operations;
import org.mule.runtime.extension.api.annotation.connectivity.ConnectionProviders;
import org.mule.runtime.extension.api.annotation.dsl.xml.Xml;


/**
 * This is the main class of an extension, is the entry point from which configurations, connection providers, operations
 * and sources are going to be declared.
 */
@Xml(prefix = "connection-pool-plugin")
@Extension(name = "connection-pool-plugin")
@Operations(ConnectionPoolStatsOperations.class)
@ConnectionProviders({C3P0MbeanConnectionProvider.class, OracleUCPConnectionProvider.class})
public class ConnectionPoolStatsExtension {

}
