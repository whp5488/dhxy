package com.kitty.system.game.role.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;
import lombok.Getter;
import lombok.Setter;

import java.beans.ConstructorProperties;

@Setter
@Getter
@MessageMeta(module = 4323)
public class RespLevelUp extends Message {
  private int roleId;
  
  private int level;
  
  public RespLevelUp() {}
  
  @ConstructorProperties({"roleId", "level"})
  public RespLevelUp(int roleId, int level) {
    this.roleId = roleId;
    this.level = level;
  }

}
