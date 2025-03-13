package net.somta.juggle.console.interfaces.controller;

import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import java.util.Set;


public class TestController {

    public static void main(String[] args) throws Exception {
        //远程服务器需要设置jvm参数：
//                -Dcom.sun.management.jmxremote
//                -Dcom.sun.management.jmxremote.port=1099
//                -Dcom.sun.management.jmxremote.authenticate=false
//                -Dcom.sun.management.jmxremote.ssl=false
        // 连接到远程 JMX
        String url = "service:jmx:rmi:///jndi/rmi://localhost:1099/jmxrmi";
        JMXServiceURL jmxUrl = new JMXServiceURL(url);
        JMXConnector jmxConnector = JMXConnectorFactory.connect(jmxUrl, null);
        MBeanServerConnection mBeanServer = jmxConnector.getMBeanServerConnection();

        // 获取所有 CamelContext
        Set<ObjectName> camelContexts = mBeanServer.queryNames(new ObjectName("org.apache.camel:type=context,*"), null);
        for (ObjectName context : camelContexts) {
            String camelContextName = (String) mBeanServer.getAttribute(context, "CamelId");
            System.out.println("Camel Context: " + camelContextName);
        }

        // 获取某个具体的路由信息
        Set<ObjectName> camelRoutes = mBeanServer.queryNames(new ObjectName("org.apache.camel:context=*,type=routes,name=*"), null);
        for (ObjectName route : camelRoutes) {
            String routeId = (String) mBeanServer.getAttribute(route, "RouteId");
            String state = (String) mBeanServer.getAttribute(route, "State");
            System.out.println("Route ID: " + routeId + " | State: " + state);
        }

        // 关闭连接
        jmxConnector.close();
    }
}
