
package com.kitty.cxf;

import org.apache.cxf.common.util.Compiler;
import java.io.File;
import java.util.List;
import org.apache.cxf.bus.CXFBusFactory;
import org.apache.cxf.jaxws.support.JaxWsEndpointImplFactory;
import org.apache.cxf.endpoint.EndpointImplFactory;
import org.apache.cxf.Bus;
import org.apache.cxf.endpoint.dynamic.DynamicClientFactory;

public class JaxWsDynamicClientFactory extends DynamicClientFactory
{
    protected JaxWsDynamicClientFactory(final Bus bus) {
        super(bus);
    }
    
    protected EndpointImplFactory getEndpointImplFactory() {
        return JaxWsEndpointImplFactory.getSingleton();
    }
    
    protected boolean allowWrapperOps() {
        return true;
    }
    
    public static JaxWsDynamicClientFactory newInstance(final Bus b) {
        return new JaxWsDynamicClientFactory(b);
    }
    
    public static JaxWsDynamicClientFactory newInstance() {
        final Bus bus = CXFBusFactory.getThreadDefaultBus();
        return new JaxWsDynamicClientFactory(bus);
    }
    
    protected boolean compileJavaSrc(final String classPath, final List<File> srcList, final String dest) {
        final Compiler javaCompiler = new Compiler();
        javaCompiler.setEncoding("UTF-8");
        javaCompiler.setClassPath(classPath);
        javaCompiler.setOutputDir(dest);
        javaCompiler.setTarget("1.6");
        return javaCompiler.compileFiles((List)srcList);
    }
}
