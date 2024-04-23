package com.kitty.mina.message;

import com.kitty.common.utils.ClassScanner;
import com.kitty.mina.annotation.MessageMeta;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public enum MessageFactory {
  INSTANCE;
  
  private Map<Class<?>, String> clazz2Id;
  
  private Map<String, Class<?>> id2Clazz;
  
  MessageFactory() {
    this.id2Clazz = new HashMap<>();
    this.clazz2Id = new HashMap<>();
  }
  
  public void initMessagePool(String scanPath) {
    Set<Class<?>> messages = ClassScanner.listAllSubclasses(scanPath, Message.class);
    for (Class<?> clazz : messages) {
      MessageMeta meta = clazz.<MessageMeta>getAnnotation(MessageMeta.class);
      if (meta == null)
        throw new RuntimeException("messages[" + clazz.getSimpleName() + "] missed MessageMeta annotation"); 
      String key = buildKey(meta.module(), meta.cmd());
      if (this.id2Clazz.containsKey(key))
        throw new RuntimeException("message message [" + key + "] duplicate！！"); 
      this.id2Clazz.put(key, clazz);
      this.clazz2Id.put(clazz, key);
    } 
  }
  
  public Class<?> getMessage(int module, int cmd) {
    return this.id2Clazz.get(buildKey(module, cmd));
  }
  
  public Class<?> getMessage(int id) {
    short module = (short)(id / 1000);
    short cmd = (short)(id % 1000);
    return this.id2Clazz.get(buildKey(module, cmd));
  }
  
  public String getMessageId(Class<?> clazz) {
    return this.clazz2Id.get(clazz);
  }
  
  private String buildKey(int module, int cmd) {
    return module + "_" + cmd;
  }
}
