package com.kitty.system.game.team.domain.vo;

import lombok.Getter;
import lombok.Setter;

import java.beans.ConstructorProperties;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class SmartTeam implements Serializable {

  List<SmartTeamMember> smartTeamMembers = new ArrayList<>();
  
  @ConstructorProperties({"smartTeamMembers"})
  public SmartTeam(List<SmartTeamMember> smartTeamMembers) {
    this.smartTeamMembers = smartTeamMembers;
  }

}
