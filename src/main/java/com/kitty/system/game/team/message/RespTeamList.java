package com.kitty.system.game.team.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;
import com.kitty.system.game.team.domain.vo.SmartTeam;
import lombok.Getter;
import lombok.Setter;

import java.beans.ConstructorProperties;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@MessageMeta(module = 4118)
public class RespTeamList extends Message {

  @ConstructorProperties({"smartTeams"})
  public RespTeamList(List<SmartTeam> smartTeams) {
    this.smartTeams = smartTeams;
  }
  
  private List<SmartTeam> smartTeams = new ArrayList<>();

}
