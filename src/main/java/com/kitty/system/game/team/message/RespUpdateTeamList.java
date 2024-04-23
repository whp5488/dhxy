package com.kitty.system.game.team.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;
import com.kitty.system.game.team.domain.TeamMember;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@MessageMeta(module = 4119)
public class RespUpdateTeamList extends Message {
  private String target;
  
  private List<TeamMember> list;

}
