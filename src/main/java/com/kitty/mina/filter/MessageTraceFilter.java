package com.kitty.mina.filter;

import com.kitty.mina.MessageStatistics;
import com.kitty.mina.cache.SessionUtils;
import com.kitty.mina.message.Message;
import com.kitty.system.game.enter.ReqMultiMoveTo;
import com.kitty.system.game.role.domain.Role;
import java.util.HashSet;
import java.util.Set;
import org.apache.mina.core.filterchain.IoFilter;
import org.apache.mina.core.filterchain.IoFilterAdapter;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.core.write.WriteRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MessageTraceFilter extends IoFilterAdapter {
  private Logger logger = LoggerFactory.getLogger(MessageTraceFilter.class);
  
  private boolean debug = true;
  
  public void messageReceived(IoFilter.NextFilter nextFilter, IoSession session, Object message) throws Exception {
    if (!this.debug || !traceRequest(message) || !"ReqEcho".equals(message.getClass().getSimpleName()));
    Message msg = (Message)message;
    MessageStatistics.getInstance().addReceived(msg.getModule());
    nextFilter.messageReceived(session, message);
  }
  
  private boolean traceRequest(Object message) {
    Set<Class<?>> ignores = new HashSet<>();
    ignores.add(ReqMultiMoveTo.class);
    return !ignores.contains(message.getClass());
  }
  
  public void messageSent(IoFilter.NextFilter nextFilter, IoSession session, WriteRequest writeRequest) throws Exception {
    Object message = writeRequest.getMessage();
    if (!this.debug || !traceResponse(message) || !"RespReplyEcho".equals(message.getClass().getSimpleName()));
    Message msg = (Message)message;
    MessageStatistics.getInstance().addSent(msg.getModule());
    nextFilter.messageSent(session, writeRequest);
  }
  
  private boolean traceResponse(Object message) {
    Set<Class<?>> ignores = new HashSet<>();
    return !ignores.contains(message.getClass());
  }
  
  private String getMessageSignure(IoSession session) {
    Role role = SessionUtils.getRoleBySession(session);
    if (role != null)
      return role.getName(); 
    return String.valueOf(session);
  }
}
