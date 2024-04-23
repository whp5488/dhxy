package com.kitty.mina.message;

import java.lang.reflect.Method;

public class CmdExecutor {
  private Method method;
  
  private Class<?>[] params;
  
  private Object handler;
  
  public static CmdExecutor valueOf(Method method, Class<?>[] params, Object handler) {
    CmdExecutor executor = new CmdExecutor();
    executor.method = method;
    executor.params = params;
    executor.handler = handler;
    return executor;
  }
  
  public Method getMethod() {
    return this.method;
  }
  
  public Class<?>[] getParams() {
    return this.params;
  }
  
  public Object getHandler() {
    return this.handler;
  }
}
