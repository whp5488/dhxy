package com.kitty.mina;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FireWallConfig {
  @Value("${mina.firewall.config.maxPackagePerSecond}")
  private int maxPackagePerSecond;
  
  @Value("${mina.firewall.config.floodWindowSeconds}")
  private int floodWindowSeconds;
  
  @Value("${mina.firewall.config.maxFloodTimes}")
  private int maxFloodTimes;
  
  @Value("${mina.firewall.config.anonymousAliveMilli}")
  private int anonymousAliveMilli;
  
  private static volatile FireWallConfig self;
  
  public int getMaxPackagePerSecond() {
    return this.maxPackagePerSecond;
  }
  
  public void setMaxPackagePerSecond(int maxPackagePerSecond) {
    this.maxPackagePerSecond = maxPackagePerSecond;
  }
  
  public int getFloodWindowSeconds() {
    return this.floodWindowSeconds;
  }
  
  public void setFloodWindowSeconds(int floodWindowSeconds) {
    this.floodWindowSeconds = floodWindowSeconds;
  }
  
  public int getMaxFloodTimes() {
    return this.maxFloodTimes;
  }
  
  public void setMaxFloodTimes(int maxFloodTimes) {
    this.maxFloodTimes = maxFloodTimes;
  }
  
  public int getAnonymousAliveMilli() {
    return this.anonymousAliveMilli;
  }
  
  public String toString() {
    return "FireWallConfig [maxPackagePerSecond=" + this.maxPackagePerSecond + ", floodWindowSeconds=" + this.floodWindowSeconds + ", maxFloodTimes=" + this.maxFloodTimes + "]";
  }
}
