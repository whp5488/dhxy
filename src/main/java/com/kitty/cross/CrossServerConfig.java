package com.kitty.cross;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class CrossServerConfig {
  @Value("${cross.center.ip}")
  private String centerIp;
  
  @Value("${cross.center.rpcPort}")
  private int centerPort;
  
  @Value("${cross.center.gamePort}")
  private int gamePort;
  
  @Value("${cross.center.bit}")
  private int isCenter;
  
  public boolean isCenterServer() {
    return (this.isCenter > 0);
  }
  
  public String getCenterIp() {
    return this.centerIp;
  }
  
  public int getCenterPort() {
    return this.centerPort;
  }
  
  public int getGamePort() {
    return this.gamePort;
  }
}
