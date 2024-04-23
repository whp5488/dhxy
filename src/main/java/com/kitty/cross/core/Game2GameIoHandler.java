package com.kitty.cross.core;

import com.kitty.cross.core.server.CMessageDispatcher;
import com.kitty.cross.core.server.SCSession;
import com.kitty.mina.message.Message;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.AttributeKey;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Game2GameIoHandler extends IoHandlerAdapter {
  private static Logger logger = LoggerFactory.getLogger(Game2GameIoHandler.class);
  
  private CMessageDispatcher messageDispatcher;
  
  private AttributeKey attrKey = new AttributeKey(AttributeKey.class, "SESSION_CONTAINER");
  
  public Game2GameIoHandler(CMessageDispatcher messageDispatcher) {
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
    logger.error("跨服session关闭");
  }
  
  public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
    if (!(cause instanceof java.io.IOException))
      logger.error("server exception", cause); 
  }
}
