package com.kitty.system.game.team.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;
import com.kitty.system.game.team.domain.vo.SmartTeamMember;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@MessageMeta(module = 4117)
public class RespSmartTeamMemerList extends Message {
  private String serial;
  
  private String name;

  private List<SmartTeamMember> teamMemberList = new ArrayList<>();

}
