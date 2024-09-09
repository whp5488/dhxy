package com.kitty.cxf;

import org.apache.cxf.message.Message;
import org.apache.cxf.interceptor.Fault;
import org.w3c.dom.Element;
import org.w3c.dom.Document;
import java.util.List;
import org.apache.cxf.headers.Header;
import javax.xml.namespace.QName;
import org.apache.cxf.helpers.DOMUtils;
import org.apache.cxf.binding.soap.SoapMessage;
import org.apache.cxf.phase.AbstractPhaseInterceptor;

public class ClientCheckInterceptor extends AbstractPhaseInterceptor<SoapMessage>
{
    private String USERNAME;
    private String PASSWORD;
    
    public ClientCheckInterceptor(String username, String password) {
        super("prepare-send");
        this.USERNAME = "root";
        this.PASSWORD = "root";
        this.USERNAME = username;
        this.PASSWORD = password;
    }
    
    public void handleMessage(SoapMessage message) throws Fault {
        List<Header> headers = message.getHeaders();
        Document document = DOMUtils.createDocument();
        Element authorEle = document.createElement("author");
        authorEle.setAttribute("username", this.USERNAME);
        authorEle.setAttribute("password", this.PASSWORD);
        headers.add(new Header(new QName(""), authorEle));
    }
}
