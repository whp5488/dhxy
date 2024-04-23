package com.kitty.mina.task;

import com.kitty.mina.cache.SessionUtils;
import com.kitty.mina.session.SessionManager;
import com.kitty.system.game.role.domain.Role;
import java.util.logging.Logger;
import org.apache.mina.core.session.IoSession;

public class ThreadLocalUtil {
  public static void addLocalTask(IoSession session, Runnable task) {
    if (session == null)
      throw new NullPointerException("session is null"); 
    Role role = SessionUtils.getRoleBySession(session);
    addLocalTask(role, task);
  }
  
  public static void addLocalTask(final Role role, final Runnable task) {
    if (role == null) {
      Logger.getLogger("------------------------role is null");
      throw new NullPointerException("role is null");
    } 
    Logger.getLogger("------------------------ThreadLocalUtil");
    TaskHandlerContext.INSTANCE.acceptTask(new AbstractDistributeTask() {
          int distributeKey = role.getDistributeKey();
          
          public int distributeKey() {
            return this.distributeKey;
          }
          
          public void action() {
            task.run();
          }
        });
  }
  
  public static void addLocalTask(final Runnable task) {
    Logger.getLogger("------------------------ThreadLocalUtil");
    TaskHandlerContext.INSTANCE.acceptTask(new AbstractDistributeTask() {
          int distributeKey = SessionManager.INSTANCE.getNextDistributeKey();
          
          public int distributeKey() {
            return this.distributeKey;
          }
          
          public void action() {
            task.run();
          }
        });
  }
}
