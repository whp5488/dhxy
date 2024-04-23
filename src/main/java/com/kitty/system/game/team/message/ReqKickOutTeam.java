package com.kitty.system.game.team.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@MessageMeta(module = 4120)
public class ReqKickOutTeam extends Message {
  private String name;
}
