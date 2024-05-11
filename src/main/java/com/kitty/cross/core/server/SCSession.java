
package com.kitty.cross.core.server;

import com.kitty.mina.message.Message;
import java.util.concurrent.atomic.AtomicInteger;
import org.apache.mina.core.session.IoSession;

public class SCSession
{
    private IoSession wrapper;
    private String clientIp;
    private int id;
    private static AtomicInteger idFactory;
    
    public static SCSession valueOf(final IoSession wrapper) {
        final SCSession cSession = new SCSession();
        cSession.wrapper = wrapper;
        cSession.clientIp = wrapper.getRemoteAddress().toString();
        cSession.id = SCSession.idFactory.incrementAndGet();
        return cSession;
    }
    
    public int getId() {
        return this.id;
    }
    
    public IoSession getWrapper() {
        return this.wrapper;
    }
    
    public String getClientIp() {
        return this.clientIp;
    }
    
    public void sendMessage(final Message message) {
        this.wrapper.write((Object)message);
    }
    
    static {
        SCSession.idFactory = new AtomicInteger();
    }
}
