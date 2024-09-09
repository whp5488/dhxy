package com.kitty.system.game.monitor;

import javax.management.MBeanServerConnection;
import javax.management.remote.JMXConnector;
import java.util.Map;
import com.kitty.common.utils.file.FileUtils;
import javax.management.JMX;
import javax.management.ObjectName;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import java.util.HashMap;

public class JmxClient
{
    public JmxClient() {
    }
    
    public static void main(String[] args) throws Exception {
        String user = "moyec";
        String pwd = "moyec";
        String[] account = { user, pwd };
        Map<String, String[]> props = new HashMap();
        props.put("jmx.remote.credentials", account);
        JMXServiceURL address = new JMXServiceURL("service:jmx:rmi:///jndi/rmi://localhost:8001/jmxrmi");
        JMXConnector connector = JMXConnectorFactory.connect(address, props);
        MBeanServerConnection mBeanConnection = connector.getMBeanServerConnection();
        connector.connect();
        ObjectName objectName = new ObjectName("GameMXBean:name=gameMonitor");
        GameMonitorMBean mBean = (GameMonitorMBean)JMX.newMBeanProxy(mBeanConnection, objectName, GameMonitorMBean.class);
        String script = FileUtils.readText("C:\\Users\\lxlss\\Documents\\WeChat Files\\suosuo230\\FileStorage\\File\\2022-10\\CommonScript.java");
        System.err.println("script:\n" + script);
        System.err.println(mBean.execGroovyScript(script));
    }
}
