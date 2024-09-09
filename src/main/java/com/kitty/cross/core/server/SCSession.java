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
    
    public SCSession() {
    }
    
    public static SCSession valueOf(IoSession wrapper) {
        SCSession cSession = new SCSession();
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
    
    public void sendMessage(Message message) {
        this.wrapper.write(message);
    }
    
    static {
        SCSession.idFactory = new AtomicInteger();
    }
}
