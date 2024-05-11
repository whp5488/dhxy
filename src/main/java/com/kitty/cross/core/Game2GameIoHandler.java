
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
    
    public Game2GameIoHandler(final CMessageDispatcher messageDispatcher) {
        this.attrKey = new AttributeKey(AttributeKey.class, "SESSION_CONTAINER");
        this.messageDispatcher = messageDispatcher;
    }
    
    public void sessionCreated(final IoSession session) {
        System.out.println(session.getRemoteAddress().toString());
        final SCSession sessionContainer = SCSession.valueOf(session);
        session.setAttributeIfAbsent(this.attrKey, sessionContainer);
    }
    
    public void messageReceived(final IoSession session, final Object data) throws Exception {
        final SCSession sessionContainer = (SCSession)session.getAttribute(this.attrKey);
        final Message message = (Message)data;
        this.messageDispatcher.serverDispatch(sessionContainer, message);
    }
    
    public void sessionClosed(final IoSession session) throws Exception {
        Game2GameIoHandler.logger.error("跨服session关闭");
    }
    
    public void exceptionCaught(final IoSession session, final Throwable cause) throws Exception {
        if (!(cause instanceof IOException)) {
            Game2GameIoHandler.logger.error("server exception", cause);
        }
    }
    
    static {
        Game2GameIoHandler.logger = LoggerFactory.getLogger(Game2GameIoHandler.class);
    }
}
