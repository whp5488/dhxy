
package com.kitty.cxf.common;

import org.apache.cxf.binding.soap.SoapMessage;
import org.apache.cxf.headers.Header;
import org.apache.cxf.helpers.DOMUtils;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.namespace.QName;
import java.util.List;

public class ClientLoginInterceptor extends AbstractPhaseInterceptor<SoapMessage>
{
    private String username;
    private String password;
    
    public ClientLoginInterceptor() {
        super("prepare-send");
    }
    
    public ClientLoginInterceptor(final String username, final String password) {
        super("prepare-send");
        this.username = username;
        this.password = password;
    }
    
    public void handleMessage(final SoapMessage soap) throws Fault {
        final List<Header> headers = soap.getHeaders();
        final Document doc = DOMUtils.createDocument();
        final Element auth = doc.createElement("authrity");
        final Element username = doc.createElement("username");
        final Element password = doc.createElement("password");
        username.setTextContent(this.username);
        password.setTextContent(this.password);
        auth.appendChild(username);
        auth.appendChild(password);
        headers.add(0, new Header(new QName("timamaes"), auth));
    }
}
