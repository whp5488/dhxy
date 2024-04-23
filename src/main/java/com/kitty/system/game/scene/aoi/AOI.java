package com.kitty.system.game.scene.aoi;

import com.kitty.mina.message.Message;
import com.kitty.system.game.role.domain.Role;
import java.util.Set;

public abstract class AOI {
  private int id;
  
  public abstract void enter(Role paramRole);
  
  public abstract void leave(long paramLong);
  
  public abstract void move(Role paramRole);
  
  public abstract void goTo(Role paramRole);
  
  public abstract void sendMessages(Role paramRole, Message... paramVarArgs);
  
  public abstract void giveAppear(Role paramRole, Set<Long> paramSet);
  
  public int getId() {
    return this.id;
  }
  
  public void setId(int id) {
    this.id = id;
  }
}
