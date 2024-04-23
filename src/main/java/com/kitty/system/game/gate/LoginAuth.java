package com.kitty.system.game.gate;

import java.util.Random;

public class LoginAuth {
  private int authKey;
  
  private int seed;
  
  private long uid;
  
  public void setAuthKey(int authKey) {
    this.authKey = authKey;
  }
  
  public void setSeed(int seed) {
    this.seed = seed;
  }
  
  public void setUid(long uid) {
    this.uid = uid;
  }
  
  public int getAuthKey() {
    return this.authKey;
  }
  
  public int getSeed() {
    return this.seed;
  }
  
  public long getUid() {
    return this.uid;
  }
  
  public LoginAuth() {
    Random random = new Random();
    this.authKey = random.nextInt(8888888) + 1111111;
    this.seed = random.nextInt(888888888) + 111111111;
  }
}
