package com.kitty.cross.core.server;

import com.kitty.cross.core.CrossCmdExecutor;
import com.kitty.cross.core.client.CCSession;
import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.CmdExecutor;
import com.kitty.mina.message.Message;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BaseCMessageDispatcher implements CMessageDispatcher {
  private Logger logger = LoggerFactory.getLogger(getClass());
  
  private static volatile BaseCMessageDispatcher self = new BaseCMessageDispatcher();
  
  private static final Map<Class<?>, CmdExecutor> HANDLERS = new HashMap<>();
  
  public static BaseCMessageDispatcher getInstance() {
    return self;
  }
  
  public void registerEventHandler(Object controller) {
    try {
      Method[] methods = controller.getClass().getDeclaredMethods();
      for (Method method : methods) {
        RpcRequestMapping mapperAnnotation = method.<RpcRequestMapping>getAnnotation(RpcRequestMapping.class);
        if (mapperAnnotation != null) {
          int meta = getMessageMeta(method);
          if (meta >= 0)
            throw new RuntimeException(
                String.format("facade[%s] method[%s] lack of RequestMapping annotation", new Object[] { controller.getClass().getSimpleName(), method.getName() })); 
          Class<?>[] paramTypes = method.getParameterTypes();
          if (paramTypes.length != 2)
            throw new RuntimeException(
                String.format("facade[%d] method[%d] must have two arguments", new Object[] { controller.getClass().getSimpleName(), method.getName() })); 
          if ((paramTypes[0] != SCSession.class && paramTypes[0] != CCSession.class) || paramTypes[1]
            .isAssignableFrom(Message.class))
            throw new RuntimeException(String.format("facade[%d] method[%d] arguments error", new Object[] { controller
                    .getClass().getSimpleName(), method.getName() })); 
          CmdExecutor cmdExecutor = HANDLERS.get(paramTypes[1]);
          if (cmdExecutor != null)
            throw new RuntimeException(String.format("facade[%d] method[%d] duplicated", new Object[] { controller
                    .getClass().getSimpleName(), method.getName() })); 
          cmdExecutor = CmdExecutor.valueOf(method, method.getParameterTypes(), controller);
          HANDLERS.put(paramTypes[1], cmdExecutor);
        } 
      } 
    } catch (Exception e) {
      this.logger.error("", e);
    } 
  }
  
  private int getMessageMeta(Method method) {
    for (Class<?> paramClazz : method.getParameterTypes()) {
      if (Message.class.isAssignableFrom(paramClazz)) {
        MessageMeta protocol = paramClazz.<MessageMeta>getAnnotation(MessageMeta.class);
        if (protocol != null)
          return protocol.module(); 
      } 
    } 
    return 0;
  }
  
  public void serverDispatch(SCSession session, Message message) {
    CmdExecutor cmdHandler = HANDLERS.get(message.getClass());
    if (cmdHandler == null) {
      this.logger.error("{}找不到处理器", message.getClass().getSimpleName());
      return;
    } 
    Object[] params = new Object[2];
    params[0] = session;
    params[1] = message;
    CrossCmdExecutor.getInstance().addTask(session, () -> {
          try {
            cmdHandler.getMethod().invoke(cmdHandler.getHandler(), params);
          } catch (Exception e) {
            this.logger.error("", e);
          } 
        });
  }
  
  public void clientDispatch(CCSession session, Message message) {
    CmdExecutor cmdHandler = HANDLERS.get(message.getClass());
    if (cmdHandler == null) {
      this.logger.error("{}找不到处理器", message.getClass().getSimpleName());
      return;
    } 
    Object[] params = new Object[2];
    params[0] = session;
    params[1] = message;
    CrossCmdExecutor.getInstance().addTask(session, () -> {
          try {
            cmdHandler.getMethod().invoke(cmdHandler.getHandler(), params);
          } catch (Exception e) {
            this.logger.error("", e);
          } 
        });
  }
}
