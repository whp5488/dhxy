package com.kitty.cxf.common;

import java.util.List;
import javax.xml.namespace.QName;
import org.apache.cxf.binding.soap.SoapMessage;
import org.apache.cxf.headers.Header;
import org.apache.cxf.helpers.DOMUtils;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class ClientLoginInterceptor extends AbstractPhaseInterceptor<SoapMessage> {
  private String username;
  
  private String password;
  
  public ClientLoginInterceptor() {
    super("prepare-send");
  }
  
  public ClientLoginInterceptor(String username, String password) {
    super("prepare-send");
    this.username = username;
    this.password = password;
  }
  
  public void handleMessage(SoapMessage soap) throws Fault {
    List<Header> headers = soap.getHeaders();
    Document doc = DOMUtils.createDocument();
    Element auth = doc.createElement("authrity");
    Element username = doc.createElement("username");
    Element password = doc.createElement("password");
    username.setTextContent(this.username);
    password.setTextContent(this.password);
    auth.appendChild(username);
    auth.appendChild(password);
    headers.add(0, new Header(new QName("timamaes"), auth));
  }
}
