
package com.kitty.cxf;

import com.kitty.cxf.common.AuthInterceptor;
import com.kitty.cxf.common.ClientLoginInterceptor;
import org.apache.cxf.jaxws.EndpointImpl;
import javax.xml.ws.Endpoint;
import org.springframework.context.annotation.Bean;
import javax.servlet.Servlet;
import org.apache.cxf.transport.servlet.CXFServlet;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import com.kitty.cxf.service.ServiceMaster;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.cxf.Bus;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CxfConfig
{
    @Autowired
    private Bus bus;
    @Autowired
    private ServiceMaster serviceMaster;
    
    @Bean
    public ServletRegistrationBean cxfServlet() {
        return new ServletRegistrationBean((Servlet)new CXFServlet(), new String[] { "/services/*" });
    }
    
    @Bean
    public Endpoint endpoint() {
        final EndpointImpl endpoint = new EndpointImpl(this.bus, (Object)this.serviceMaster);
        endpoint.publish("/ServiceMaster");
        endpoint.getInInterceptors().add(new ClientLoginInterceptor());
        endpoint.getInInterceptors().add(new AuthInterceptor());
        return (Endpoint)endpoint;
    }
}
