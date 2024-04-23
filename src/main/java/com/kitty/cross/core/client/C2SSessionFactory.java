package com.kitty.cross.core.client;

import com.kitty.cross.core.server.BaseCMessageDispatcher;
import com.kitty.cross.core.server.CMessageDispatcher;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;

class C2SSessionFactory extends BasePooledObjectFactory<CCSession> {
  String ip;
  
  int port;
  
  CMessageDispatcher dispatcher;
  
  public C2SSessionFactory(String ip, int port) {
    this.ip = ip;
    this.port = port;
    this.dispatcher = (CMessageDispatcher)BaseCMessageDispatcher.getInstance();
  }
  
  public String getIp() {
    return this.ip;
  }
  
  public void setIp(String ip) {
    this.ip = ip;
  }
  
  public int getPort() {
    return this.port;
  }
  
  public void setPort(int port) {
    this.port = port;
  }
  
  public CCSession create() throws Exception {
    CCSession session = CCSession.valueOf(this.ip, this.port, this.dispatcher);
    session.buildConnection();
    return session;
  }
  
  public PooledObject<CCSession> wrap(CCSession obj) {
    return (PooledObject<CCSession>)new DefaultPooledObject(obj);
  }
}
