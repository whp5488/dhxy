
package com.kitty.cross.core.server;

import java.util.HashMap;
import com.kitty.cross.core.CrossCmdExecutor;
import com.kitty.mina.annotation.MessageMeta;
import java.lang.reflect.Method;
import com.kitty.mina.message.Message;
import com.kitty.cross.core.client.CCSession;
import org.slf4j.LoggerFactory;
import com.kitty.mina.message.CmdExecutor;
import java.util.Map;
import org.slf4j.Logger;

public class BaseCMessageDispatcher implements CMessageDispatcher
{
    private Logger logger;
    private static volatile BaseCMessageDispatcher self;
    private static final Map<Class<?>, CmdExecutor> HANDLERS;
    
    public BaseCMessageDispatcher() {
        this.logger = LoggerFactory.getLogger(this.getClass());
    }
    
    public static BaseCMessageDispatcher getInstance() {
        return BaseCMessageDispatcher.self;
    }
    
    public void registerEventHandler(final Object controller) {
        try {
            final Method[] declaredMethods;
            final Method[] methods = declaredMethods = controller.getClass().getDeclaredMethods();
            for (final Method method : declaredMethods) {
                final RpcRequestMapping mapperAnnotation = method.getAnnotation(RpcRequestMapping.class);
                if (mapperAnnotation != null) {
                    final int meta = this.getMessageMeta(method);
                    if (meta >= 0) {
                        throw new RuntimeException(String.format("facade[%s] method[%s] lack of RequestMapping annotation", controller.getClass().getSimpleName(), method.getName()));
                    }
                    final Class<?>[] paramTypes = method.getParameterTypes();
                    if (paramTypes.length != 2) {
                        throw new RuntimeException(String.format("facade[%d] method[%d] must have two arguments", controller.getClass().getSimpleName(), method.getName()));
                    }
                    if ((paramTypes[0] != SCSession.class && paramTypes[0] != CCSession.class) || paramTypes[1].isAssignableFrom(Message.class)) {
                        throw new RuntimeException(String.format("facade[%d] method[%d] arguments error", controller.getClass().getSimpleName(), method.getName()));
                    }
                    CmdExecutor cmdExecutor = BaseCMessageDispatcher.HANDLERS.get(paramTypes[1]);
                    if (cmdExecutor != null) {
                        throw new RuntimeException(String.format("facade[%d] method[%d] duplicated", controller.getClass().getSimpleName(), method.getName()));
                    }
                    cmdExecutor = CmdExecutor.valueOf(method, method.getParameterTypes(), controller);
                    BaseCMessageDispatcher.HANDLERS.put(paramTypes[1], cmdExecutor);
                }
            }
        }
        catch (final Exception e) {
            this.logger.error("", e);
        }
    }
    
    private int getMessageMeta(final Method method) {
        for (final Class<?> paramClazz : method.getParameterTypes()) {
            if (Message.class.isAssignableFrom(paramClazz)) {
                final MessageMeta protocol = paramClazz.getAnnotation(MessageMeta.class);
                if (protocol != null) {
                    return protocol.module();
                }
            }
        }
        return 0;
    }
    
    @Override
    public void serverDispatch(final SCSession session, final Message message) {
        final CmdExecutor cmdHandler = BaseCMessageDispatcher.HANDLERS.get(message.getClass());
        if (cmdHandler == null) {
            this.logger.error("{}找不到处理器", (Object)message.getClass().getSimpleName());
            return;
        }
        final Object[] params = { session, message };
        CrossCmdExecutor.getInstance().addTask(session, () -> {
            try {
                cmdHandler.getMethod().invoke(cmdHandler.getHandler(), params);
            }
            catch (final Exception e) {
                this.logger.error("", e);
            }
        });
    }
    
    @Override
    public void clientDispatch(final CCSession session, final Message message) {
        final CmdExecutor cmdHandler = BaseCMessageDispatcher.HANDLERS.get(message.getClass());
        if (cmdHandler == null) {
            this.logger.error("{}找不到处理器", message.getClass().getSimpleName());
            return;
        }
        final Object[] params = { session, message };
        CrossCmdExecutor.getInstance().addTask(session, () -> {
            try {
                cmdHandler.getMethod().invoke(cmdHandler.getHandler(), params);
            }
            catch (final Exception e) {
                this.logger.error("", e);
            }
        });
    }
    
    static {
        BaseCMessageDispatcher.self = new BaseCMessageDispatcher();
        HANDLERS = new HashMap<>();
    }
}
