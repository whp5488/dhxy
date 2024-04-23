package com.kitty.cxf.utlis;

import com.kitty.logs.LoggerFunction;
import com.kitty.mina.cache.SessionUtils;
import com.kitty.system.game.role.domain.Role;
import org.apache.mina.core.future.CloseFuture;
import org.apache.mina.core.future.IoFuture;
import org.apache.mina.core.future.IoFutureListener;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

@Component
public class Promote {
  public void closeSession(final IoSession session) {
    if (session == null)
      return; 
    final Logger logger = LoggerFunction.MONOIOR.getLogger();
    Role role = SessionUtils.getRoleBySession(session);
    try {
      final String name = (role != null) ? role.getName() : "";
      if (session.isConnected() && !session.isClosing()) {
        CloseFuture future = session.closeNow();
        future.addListener(new IoFutureListener<IoFuture>() {
              public void operationComplete(IoFuture future) {
                logger.error("session[{}] closed complete,player[{}]", Long.valueOf(session.getId()), name);
              }
            });
      } 
    } catch (Exception e) {
      logger.error("session[{}] closed complete exception, player[{}]", Long.valueOf(session.getId()), role.getName());
      e.getMessage();
    } 
  }
}
