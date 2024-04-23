package com.kitty.system.game.role.domain;

import com.kitty.system.game.player.PlayerProfile;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RoleInfo {
  private String sole;
  
  private int id;
  
  private String name;
  
  private byte sex;
  
  private short level;
  
  private byte relive;
  
  private byte race;
  
  private short icon;
  
  private int serial;
  
  private byte drill;
  
  private String marking;

  public RoleInfo(PlayerProfile profile) {
    this.sole = String.valueOf(profile.getId());
    this.id = profile.getRoleId();
    this.name = profile.getName();
    this.sex = profile.getSex();
    this.level = (short)profile.getLevel();
    this.relive = (byte)profile.getRelive();
    this.race = profile.getRace();
    this.icon = (short)profile.getIcon();
    this.serial = profile.getSerial();
    this.drill = profile.getDrill();
    this.marking = profile.getMarking();
  }
}
