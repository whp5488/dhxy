package com.kitty.system.game.team.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@MessageMeta(module = 49152)
public class RespLookPlayerInfo extends Message {
  
  private int leaderRoleId = 0;
  
  private int roleId;
  
  private String name;
  
  private String faction;
  
  private int serial;

  private int teamMembersCount = 1;

  private byte rei = 1;

  private short level = 1;

  private byte isPK = 0;

}
