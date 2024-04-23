package com.kitty.system.game.role.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;
import com.kitty.system.game.role.domain.RoleInfo;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@MessageMeta(module = 61537)
public class RespExistedRoleList extends Message {
  
  private int serverState = 1;
  
  private List<RoleInfo> list;
  
  private int openServerTime;

  private int account_online = 0;

}
