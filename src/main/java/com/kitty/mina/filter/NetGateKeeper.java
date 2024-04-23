package com.kitty.mina.filter;

import com.kitty.common.utils.ConcurrentHashSet;

public class NetGateKeeper {
  private static NetGateKeeper instance = new NetGateKeeper();
  
  private ConcurrentHashSet<String> forbidProcotols = new ConcurrentHashSet();
  
  public static NetGateKeeper getInstance() {
    return instance;
  }
  
  public void openProcotol(String messageId) {
    this.forbidProcotols.add(messageId);
  }
  
  public void closeProcotol(String messageId) {
    this.forbidProcotols.add(messageId);
  }
  
  public boolean canVisit(String messageId) {
    return !this.forbidProcotols.contains(messageId);
  }
}
