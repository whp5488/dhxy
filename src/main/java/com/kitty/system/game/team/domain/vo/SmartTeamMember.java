package com.kitty.system.game.team.domain.vo;

import com.kitty.system.game.team.domain.Member;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
public class SmartTeamMember implements Serializable {
  private int roleId;
  
  private String name;
  
  private short level;
  
  private byte rebirth;
  
  private int serial;

  public SmartTeamMember() {}
  
  public SmartTeamMember(Member member) {
    this.roleId = member.getRoleId();
    this.name = member.getName();
    this.level = member.getLevel();
    this.rebirth = member.getRelive();
    this.serial = member.getSerial();
  }
}
