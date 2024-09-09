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
    
    public void registerEventHandler(Object controller) {
        try {
            Method[] methods;
            for (Method method : methods = controller.getClass().getDeclaredMethods()) {
                RpcRequestMapping mapperAnnotation = method.getAnnotation(RpcRequestMapping.class);
                if (mapperAnnotation != null) {
                    int meta = this.getMessageMeta(method);
                    if (meta >= 0) {
                        throw new RuntimeException(String.format("facade[%s] method[%s] lack of RequestMapping annotation", controller.getClass().getSimpleName(), method.getName()));
                    }
                    Class<?>[] paramTypes = method.getParameterTypes();
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
        catch (Exception e) {
            this.logger.error("", e);
        }
    }
    
    private int getMessageMeta(Method method) {
        for (Class<?> paramClazz : method.getParameterTypes()) {
            if (Message.class.isAssignableFrom(paramClazz)) {
                MessageMeta protocol = paramClazz.getAnnotation(MessageMeta.class);
                if (protocol != null) {
                    return protocol.module();
                }
            }
        }
        return 0;
    }
    
    @Override
    public void serverDispatch(SCSession session, Message message) {
        CmdExecutor cmdHandler = BaseCMessageDispatcher.HANDLERS.get(message.getClass());
        if (cmdHandler == null) {
            this.logger.error("{}找不到处理器", message.getClass().getSimpleName());
            return;
        }
        Object[] params = { session, message };
        CrossCmdExecutor.getInstance().addTask(session, () -> {
            try {
                cmdHandler.getMethod().invoke(cmdHandler.getHandler(), params);
            }
            catch (Exception e) {
                this.logger.error("", e);
            }
        });
    }
    
    @Override
    public void clientDispatch(CCSession session, Message message) {
        CmdExecutor cmdHandler = BaseCMessageDispatcher.HANDLERS.get(message.getClass());
        if (cmdHandler == null) {
            this.logger.error("{}找不到处理器", message.getClass().getSimpleName());
            return;
        }
        Object[] params = { session, message };
        CrossCmdExecutor.getInstance().addTask(session, () -> {
            try {
                cmdHandler.getMethod().invoke(cmdHandler.getHandler(), params);
            }
            catch (Exception e) {
                this.logger.error("", e);
            }
        });
    }
    
    static {
        BaseCMessageDispatcher.self = new BaseCMessageDispatcher();
        HANDLERS = new HashMap();
    }
}
