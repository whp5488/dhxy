
package com.kitty.common.start;

import org.apache.coyote.http11.Http11NioProtocol;
import org.apache.catalina.connector.Connector;
import org.springframework.boot.context.embedded.tomcat.TomcatConnectorCustomizer;

class MyTomcatConnectorCustomizer implements TomcatConnectorCustomizer
{
    public void customize(final Connector connector) {
        final Http11NioProtocol protocol = (Http11NioProtocol)connector.getProtocolHandler();
        protocol.setMaxConnections(2000);
        protocol.setMaxThreads(2000);
        protocol.setConnectionTimeout(3000);
    }
}
