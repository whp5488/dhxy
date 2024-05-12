
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
        this.id2Clazz = new HashMap<>();
        this.clazz2Id = new HashMap<>();
    }
    
    public void initMessagePool(final String scanPath) {
        final Set<Class<?>> messages = ClassScanner.listAllSubclasses(scanPath, Message.class);
        for (final Class<?> clazz : messages) {
            final MessageMeta meta = clazz.getAnnotation(MessageMeta.class);
            if (meta == null) {
                throw new RuntimeException("messages[" + clazz.getSimpleName() + "] missed MessageMeta annotation");
            }
            final String key = this.buildKey(meta.module(), meta.cmd());
            if (this.id2Clazz.containsKey(key)) {
                throw new RuntimeException("message message [" + key + "] duplicate！！");
            }
            this.id2Clazz.put(key, clazz);
            this.clazz2Id.put(clazz, key);
        }
    }
    
    public Class<?> getMessage(final int module, final int cmd) {
        return this.id2Clazz.get(this.buildKey(module, cmd));
    }
    
    public Class<?> getMessage(final int id) {
        final short module = (short)(id / 1000);
        final short cmd = (short)(id % 1000);
        return this.id2Clazz.get(this.buildKey(module, cmd));
    }
    
    public String getMessageId(final Class<?> clazz) {
        return this.clazz2Id.get(clazz);
    }
    
    private String buildKey(final int module, final int cmd) {
        return module + "_" + cmd;
    }
}
