
package com.kitty.cxf.send.mapper;

import org.apache.cxf.endpoint.Client;
import com.kitty.cxf.common.MapStringUtil;
import com.kitty.cxf.ClientCheckInterceptor;
import com.kitty.cxf.JaxWsDynamicClientFactory;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class SendMapper
{
    public static Map Client(final Map<String, Object> map, final String api, final String ip, final int port, final String name, final String pwd) {
        final JaxWsDynamicClientFactory dcf = JaxWsDynamicClientFactory.newInstance();
        try {
            final Client client = dcf.createClient("http://" + ip + ":" + port + "/services/ServiceMaster?wsdl");
            client.getOutInterceptors().add(new ClientCheckInterceptor(name, pwd));
            Object[] objects;
            String string = "";
            if (map != null) {
                string = MapStringUtil.getMapToString(map);
            }
            objects = client.invoke(api, string);
            return MapStringUtil.getStringToMap(objects[0].toString());
        }
        catch (final Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
