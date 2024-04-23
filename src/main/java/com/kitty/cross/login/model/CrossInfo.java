package com.kitty.cross.login.model;

public class CrossInfo {
  private int fromServer;
  
  private String fromIp;
  
  private boolean isCross;
  
  private int crossType;
  
  public void setFromServer(int fromServer) {
    this.fromServer = fromServer;
  }
  
  public void setFromIp(String fromIp) {
    this.fromIp = fromIp;
  }
  
  public void setCross(boolean isCross) {
    this.isCross = isCross;
  }
  
  public void setCrossType(int crossType) {
    this.crossType = crossType;
  }
  
  public int getFromServer() {
    return this.fromServer;
  }
  
  public String getFromIp() {
    return this.fromIp;
  }
  
  public boolean isCross() {
    return this.isCross;
  }
  
  public int getCrossType() {
    return this.crossType;
  }
}
