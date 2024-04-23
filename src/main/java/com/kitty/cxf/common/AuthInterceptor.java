package com.kitty.cxf.common;

import com.kitty.common.start.ServerService;
import com.kitty.common.utils.spring.SpringUtils;
import java.util.List;
import org.apache.cxf.binding.soap.SoapHeader;
import org.apache.cxf.binding.soap.SoapMessage;
import org.apache.cxf.headers.Header;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.opensaml.ws.soap.common.SOAPException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.w3c.dom.Element;

@Component
public class AuthInterceptor extends AbstractPhaseInterceptor<SoapMessage> {
  public AuthInterceptor() {
    super("pre-protocol");
  }
  
  public void handleMessage(SoapMessage soapMessage) throws Fault {
    List<Header> headers = soapMessage.getHeaders();
    String username = null;
    String password = null;
    if (headers == null || headers.isEmpty())
      throw new Fault(new IllegalArgumentException("找不到Header，无法验证用户信息")); 
    for (Header header : headers) {
      SoapHeader soapHeader = (SoapHeader)header;
      Element e = (Element)soapHeader.getObject();
      username = e.getAttribute("username");
      password = e.getAttribute("password");
      if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password))
        throw new Fault(new IllegalArgumentException("用户信息为空")); 
    } 
    ServerService serverService = (ServerService)SpringUtils.getBean(ServerService.class);
    if (!serverService.getUserName().equals(username) || !serverService.getPassWord().equals(password)) {
      SOAPException soapExc = new SOAPException("认证失败");
      System.err.println("用户认证信息错误");
      throw new Fault(soapExc);
    } 
  }
}
