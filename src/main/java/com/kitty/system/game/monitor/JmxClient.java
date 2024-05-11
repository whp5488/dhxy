
package com.kitty.system.game.monitor;

import javax.management.MBeanServerConnection;
import javax.management.remote.JMXConnector;
import com.kitty.common.utils.file.FileUtils;
import javax.management.JMX;
import javax.management.ObjectName;
import java.util.Map;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import java.util.HashMap;

public class JmxClient
{
    public static void main(final String[] args) throws Exception {
        final String user = "moyec";
        final String pwd = "moyec";
        final String[] account = { user, pwd };
        final Map<String, String[]> props = new HashMap<String, String[]>();
        props.put("jmx.remote.credentials", account);
        final JMXServiceURL address = new JMXServiceURL("service:jmx:rmi:///jndi/rmi://localhost:8001/jmxrmi");
        final JMXConnector connector = JMXConnectorFactory.connect(address, props);
        final MBeanServerConnection mBeanConnection = connector.getMBeanServerConnection();
        connector.connect();
        final ObjectName objectName = new ObjectName("GameMXBean:name=gameMonitor");
        final GameMonitorMBean mBean = JMX.newMBeanProxy(mBeanConnection, objectName, GameMonitorMBean.class);
        final String script = FileUtils.readText("C:\\Users\\lxlss\\Documents\\WeChat Files\\suosuo230\\FileStorage\\File\\2022-10\\CommonScript.java");
        System.err.println("script:\n" + script);
        System.err.println(mBean.execGroovyScript(script));
    }
}
