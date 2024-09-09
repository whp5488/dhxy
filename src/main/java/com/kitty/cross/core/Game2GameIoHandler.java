package com.kitty.cross.core;

import org.slf4j.LoggerFactory;
import java.io.IOException;
import com.kitty.mina.message.Message;
import com.kitty.cross.core.server.SCSession;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.core.session.AttributeKey;
import com.kitty.cross.core.server.CMessageDispatcher;
import org.slf4j.Logger;
import org.apache.mina.core.service.IoHandlerAdapter;

public class Game2GameIoHandler extends IoHandlerAdapter
{
    private static Logger logger;
    private CMessageDispatcher messageDispatcher;
    private AttributeKey attrKey;
    
    public Game2GameIoHandler(CMessageDispatcher messageDispatcher) {
        this.attrKey = new AttributeKey(AttributeKey.class, "SESSION_CONTAINER");
        this.messageDispatcher = messageDispatcher;
    }
    
    public void sessionCreated(IoSession session) {
        System.out.println(session.getRemoteAddress().toString());
        SCSession sessionContainer = SCSession.valueOf(session);
        session.setAttributeIfAbsent(this.attrKey, sessionContainer);
    }
    
    public void messageReceived(IoSession session, Object data) throws Exception {
        SCSession sessionContainer = (SCSession)session.getAttribute(this.attrKey);
        Message message = (Message)data;
        this.messageDispatcher.serverDispatch(sessionContainer, message);
    }
    
    public void sessionClosed(IoSession session) throws Exception {
        Game2GameIoHandler.logger.error("跨服session关闭");
    }
    
    public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
        if (!(cause instanceof IOException)) {
            Game2GameIoHandler.logger.error("service exception", cause);
        }
    }
    
    static {
        Game2GameIoHandler.logger = LoggerFactory.getLogger(Game2GameIoHandler.class);
    }
}
