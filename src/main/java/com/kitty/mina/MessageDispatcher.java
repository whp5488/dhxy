package com.kitty.mina;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.annotation.RequestMapping;
import com.kitty.mina.cache.SessionUtils;
import com.kitty.mina.message.CmdExecutor;
import com.kitty.mina.message.IMessageDispatcher;
import com.kitty.mina.message.Message;
import com.kitty.mina.session.SessionManager;
import com.kitty.mina.session.SessionProperties;
import com.kitty.mina.task.AbstractDistributeTask;
import com.kitty.mina.task.MessageTask;
import com.kitty.mina.task.TaskHandlerContext;
import com.kitty.system.game.role.domain.Role;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MessageDispatcher implements IMessageDispatcher {
  private Logger logger = LoggerFactory.getLogger(getClass());
  
  private static MessageDispatcher instance = new MessageDispatcher();
  
  public static MessageDispatcher getInstance() {
    return instance;
  }
  
  private static final Map<String, CmdExecutor> MODULE_CMD_HANDLERS = new HashMap<>();
  
  public void registerMessageHandler(Object controller) {
    try {
      Method[] methods = controller.getClass().getDeclaredMethods();
      for (Method method : methods) {
        RequestMapping mapperAnnotation = method.<RequestMapping>getAnnotation(RequestMapping.class);
        if (mapperAnnotation != null) {
          String meta = getMessageMeta(method);
          if (meta == null)
            throw new RuntimeException(String.format("facade[%s] method[%s] lack of RequestMapping annotation", new Object[] { controller
                    .getClass().getSimpleName(), method.getName() })); 
          CmdExecutor cmdExecutor = MODULE_CMD_HANDLERS.get(meta);
          if (cmdExecutor != null);
          cmdExecutor = CmdExecutor.valueOf(method, method.getParameterTypes(), controller);
          MODULE_CMD_HANDLERS.put(meta, cmdExecutor);
        } 
      } 
    } catch (Exception e) {
      this.logger.error("", e);
    } 
  }
  
  private String getMessageMeta(Method method) {
    for (Class<?> paramClazz : method.getParameterTypes()) {
      if (Message.class.isAssignableFrom(paramClazz)) {
        MessageMeta protocol = paramClazz.<MessageMeta>getAnnotation(MessageMeta.class);
        if (protocol != null)
          return String.valueOf(protocol.module()); 
      } 
    } 
    return null;
  }
  
  public void dispatch(IoSession session, Message message) {
    int module = message.getModule();
    CmdExecutor cmdExecutor = MODULE_CMD_HANDLERS.get(String.valueOf(module));
    if (cmdExecutor == null) {
      this.logger.error("message executor missed, module={}", Integer.valueOf(module));
      return;
    } 
    Object[] params = convertToMethodParams(session, cmdExecutor.getParams(), message);
    Object controller = cmdExecutor.getHandler();
    int distributeKey = 0;
    Role role = SessionUtils.getRoleBySession(session);
    if (role != null) {
      distributeKey = role.getDistributeKey();
    } else {
      distributeKey = ((Integer)session.getAttribute(SessionProperties.DISTRIBUTE_KEY)).intValue();
    } 
    TaskHandlerContext.INSTANCE.acceptTask((AbstractDistributeTask)MessageTask.valueOf(distributeKey, controller, cmdExecutor.getMethod(), params, message));
  }
  
  private Object[] convertToMethodParams(IoSession session, Class<?>[] methodParams, Message message) {
    Object[] result = new Object[(methodParams == null) ? 0 : methodParams.length];
    for (int i = 0; i < result.length; i++) {
      Class<?> param = methodParams[i];
      if (IoSession.class.isAssignableFrom(param)) {
        result[i] = session;
      } else if (Long.class.isAssignableFrom(param)) {
        result[i] = Long.valueOf(SessionManager.INSTANCE.getPlayerIdBy(session));
      } else if (long.class.isAssignableFrom(param)) {
        result[i] = Long.valueOf(SessionManager.INSTANCE.getPlayerIdBy(session));
      } else if (Role.class.isAssignableFrom(param)) {
        Role role = SessionUtils.getRoleBySession(session);
        result[i] = role;
      } else if (Message.class.isAssignableFrom(param)) {
        result[i] = message;
      } 
    } 
    return result;
  }
}
