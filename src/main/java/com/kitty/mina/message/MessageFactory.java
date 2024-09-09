package com.kitty.mina.message;

import java.util.Iterator;
import java.util.Set;
import com.kitty.mina.annotation.MessageMeta;
import com.kitty.common.utils.ClassScanner;
import java.util.HashMap;
import java.util.Map;

public enum MessageFactory
{
    INSTANCE;
    
    private Map<String, Class<?>> id2Clazz;
    private Map<Class<?>, String> clazz2Id;
    
    private MessageFactory() {
        this.id2Clazz = new HashMap();
        this.clazz2Id = new HashMap();
    }
    
    public void initMessagePool(String scanPath) {
        Set<Class<?>> messages = ClassScanner.listAllSubclasses(scanPath, Message.class);
        for (Class<?> clazz : messages) {
            MessageMeta meta = (MessageMeta)clazz.getAnnotation(MessageMeta.class);
            if (meta == null) {
                throw new RuntimeException("messages[" + clazz.getSimpleName() + "] missed MessageMeta annotation");
            }
            String key = this.buildKey(meta.module(), meta.cmd());
            if (this.id2Clazz.containsKey(key)) {
                throw new RuntimeException("message message [" + key + "] duplicate！！");
            }
            this.id2Clazz.put(key, clazz);
            this.clazz2Id.put(clazz, key);
        }
    }
    
    public Class<?> getMessage(int module, int cmd) {
        return (Class)this.id2Clazz.get(this.buildKey(module, cmd));
    }
    
    public Class<?> getMessage(int id) {
        short module = (short)(id / 1000);
        short cmd = (short)(id % 1000);
        return (Class)this.id2Clazz.get(this.buildKey((int)module, (int)cmd));
    }
    
    public String getMessageId(Class<?> clazz) {
        return (String)this.clazz2Id.get(clazz);
    }
    
    private String buildKey(int module, int cmd) {
        return module + "_" + cmd;
    }
}
