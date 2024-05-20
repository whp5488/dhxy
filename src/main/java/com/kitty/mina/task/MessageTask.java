
package com.kitty.mina.task;

import org.slf4j.LoggerFactory;
import java.lang.reflect.Method;
import com.kitty.mina.message.Message;
import org.slf4j.Logger;

public class MessageTask extends AbstractDistributeTask
{
    private static Logger logger;
    private long playerId;
    private Message message;
    private Object handler;
    private Method method;
    private Object[] params;
    
    public static MessageTask valueOf(final int distributeKey, final Object handler, final Method method, final Object[] params, final Message message) {
        final MessageTask msgTask = new MessageTask();
        msgTask.distributeKey = distributeKey;
        msgTask.handler = handler;
        msgTask.method = method;
        msgTask.params = params;
        msgTask.message = message;
        return msgTask;
    }
    
    @Override
    public void action() {
        try {
            this.method.invoke(this.handler, this.params);
        }
        catch (final Throwable e) {
            e.printStackTrace();
            MessageTask.logger.error("message task execute failed=协议=={}，异常={}", this.message.getModule(), this.message);
        }
    }
    
    public long getPlayerId() {
        return this.playerId;
    }
    
    public Message getMessage() {
        return this.message;
    }
    
    public Object getHandler() {
        return this.handler;
    }
    
    public Method getMethod() {
        return this.method;
    }
    
    public Object[] getParams() {
        return this.params;
    }
    
    @Override
    public String toString() {
        return this.getName() + "[" + this.handler.getClass().getName() + "@" + this.method.getName() + "]";
    }
    
    static {
        MessageTask.logger = LoggerFactory.getLogger(MessageTask.class);
    }
}
