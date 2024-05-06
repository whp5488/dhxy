package com.kitty.system.game.gate;

import lombok.Getter;
import lombok.Setter;

import java.util.Random;

@Setter
@Getter
public class LoginAuth {
  private int authKey;
  
  private int seed;
  
  private long uid;

  public LoginAuth() {
    Random random = new Random();
    this.authKey = random.nextInt(8888888) + 1111111;
    this.seed = random.nextInt(888888888) + 111111111;
  }
}
