package com.kitty.cxf.send.mapper;

import com.kitty.cxf.ClientCheckInterceptor;
import com.kitty.cxf.JaxWsDynamicClientFactory;
import com.kitty.cxf.common.MapStringUtil;
import java.util.Map;
import org.apache.cxf.endpoint.Client;
import org.springframework.stereotype.Component;

@Component
public class SendMapper {
  public static Map Client(Map<String, Object> map, String api, String ip, int port, String name, String pwd) {
    JaxWsDynamicClientFactory dcf = JaxWsDynamicClientFactory.newInstance();
    try {
      Client client = dcf.createClient("http://" + ip + ":" + port + "/services/ServiceMaster?wsdl");
      client.getOutInterceptors().add(new ClientCheckInterceptor(name, pwd));
      Object[] objects = new Object[0];
      String string = "";
      if (map != null)
        string = MapStringUtil.getMapToString(map); 
      objects = client.invoke(api, new Object[] { string });
      return MapStringUtil.getStringToMap(objects[0].toString());
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    } 
  }
}
