
package com.kitty.mina;

import java.util.HashMap;
import com.kitty.mina.session.SessionManager;
import com.kitty.system.game.role.domain.Role;
import com.kitty.mina.task.AbstractDistributeTask;
import com.kitty.mina.task.MessageTask;
import com.kitty.mina.task.TaskHandlerContext;
import com.kitty.mina.session.SessionProperties;
import com.kitty.mina.cache.SessionUtils;
import org.apache.mina.core.session.IoSession;
import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;
import java.lang.reflect.Method;
import com.kitty.mina.annotation.RequestMapping;
import org.slf4j.LoggerFactory;
import com.kitty.mina.message.CmdExecutor;
import java.util.Map;
import org.slf4j.Logger;
import com.kitty.mina.message.IMessageDispatcher;

public class MessageDispatcher implements IMessageDispatcher
{
    private Logger logger;
    private static MessageDispatcher instance;
    private static final Map<String, CmdExecutor> MODULE_CMD_HANDLERS;
    
    public MessageDispatcher() {
        this.logger = LoggerFactory.getLogger(this.getClass());
    }
    
    public static MessageDispatcher getInstance() {
        return MessageDispatcher.instance;
    }
    
    public void registerMessageHandler(final Object controller) {
        try {
            final Method[] declaredMethods;
            final Method[] methods = declaredMethods = controller.getClass().getDeclaredMethods();
            for (final Method method : declaredMethods) {
                final RequestMapping mapperAnnotation = method.getAnnotation(RequestMapping.class);
                if (mapperAnnotation != null) {
                    final String meta = this.getMessageMeta(method);
                    if (meta == null) {
                        throw new RuntimeException(String.format("facade[%s] method[%s] lack of RequestMapping annotation", controller.getClass().getSimpleName(), method.getName()));
                    }
                    CmdExecutor cmdExecutor = MessageDispatcher.MODULE_CMD_HANDLERS.get(meta);
                    if (cmdExecutor != null) {}
                    cmdExecutor = CmdExecutor.valueOf(method, method.getParameterTypes(), controller);
                    MessageDispatcher.MODULE_CMD_HANDLERS.put(meta, cmdExecutor);
                }
            }
        }
        catch (final Exception e) {
            this.logger.error("", e);
        }
    }
    
    private String getMessageMeta(final Method method) {
        for (final Class<?> paramClazz : method.getParameterTypes()) {
            if (Message.class.isAssignableFrom(paramClazz)) {
                final MessageMeta protocol = paramClazz.getAnnotation(MessageMeta.class);
                if (protocol != null) {
                    return String.valueOf(protocol.module());
                }
            }
        }
        return null;
    }
    
    @Override
    public void dispatch(final IoSession session, final Message message) {
        final int module = message.getModule();
        final CmdExecutor cmdExecutor = MessageDispatcher.MODULE_CMD_HANDLERS.get(String.valueOf(module));
        if (cmdExecutor == null) {
            this.logger.error("message executor missed, module={}", module);
            return;
        }
        final Object[] params = this.convertToMethodParams(session, cmdExecutor.getParams(), message);
        final Object controller = cmdExecutor.getHandler();
        int distributeKey = 0;
        final Role role = SessionUtils.getRoleBySession(session);
        if (role != null) {
            distributeKey = role.getDistributeKey();
        }
        else {
            distributeKey = (int)session.getAttribute(SessionProperties.DISTRIBUTE_KEY);
        }
        TaskHandlerContext.INSTANCE.acceptTask(MessageTask.valueOf(distributeKey, controller, cmdExecutor.getMethod(), params, message));
    }
    
    private Object[] convertToMethodParams(final IoSession session, final Class<?>[] methodParams, final Message message) {
        final Object[] result = new Object[(methodParams == null) ? 0 : methodParams.length];
        for (int i = 0; i < result.length; ++i) {
            final Class<?> param = methodParams[i];
            if (IoSession.class.isAssignableFrom(param)) {
                result[i] = session;
            }
            else if (Long.class.isAssignableFrom(param)) {
                result[i] = SessionManager.INSTANCE.getPlayerIdBy(session);
            }
            else if (Long.TYPE.isAssignableFrom(param)) {
                result[i] = SessionManager.INSTANCE.getPlayerIdBy(session);
            }
            else if (Role.class.isAssignableFrom(param)) {
                final Role role = SessionUtils.getRoleBySession(session);
                result[i] = role;
            }
            else if (Message.class.isAssignableFrom(param)) {
                result[i] = message;
            }
        }
        return result;
    }
    
    static {
        MessageDispatcher.instance = new MessageDispatcher();
        MODULE_CMD_HANDLERS = new HashMap<>();
    }
}
