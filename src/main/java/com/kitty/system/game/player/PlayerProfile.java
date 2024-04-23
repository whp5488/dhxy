package com.kitty.system.game.player;

import com.kitty.common.utils.JsonUtils;
import com.kitty.common.utils.ZipUtil;
import com.kitty.system.common.formula.FormulaHero;
import com.kitty.system.game.player.bean.PlayerExtendBox;
import com.kitty.system.game.role.domain.Role;
import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

@Setter
@Getter
public class PlayerProfile implements Serializable {
  private String sid;
  
  private Integer roleId;
  
  private Long id;
  
  private String name;
  
  private int icon;
  
  private int serial;
  
  private int level;
  
  private long exp;
  
  private long nextExp;
  
  private int relive;
  
  private byte race;
  
  private long enterTime;
  
  private long lastLoginTime;
  
  private byte sex;
  
  private int faction;

  private byte drill = 0;
  
  private String marking;

  public PlayerProfile(Role role) {
    this.sid = role.getSid();
    this.id = role.getId();
    this.name = role.getName();
    this.icon = role.getIcon();
    this.serial = role.getSerial().intValue();
    this.level = role.getLevel().intValue();
    this.exp = role.getExp();
    this.nextExp = FormulaHero.formulaHeroLvUpExp(role.getLevel(), Integer.valueOf(role.getLevel().intValue() + 1), role.getRelive());
    this.roleId = role.getRoleId();
    this.race = role.getRace().byteValue();
    this.sex = role.getSex().byteValue();
    this.relive = role.getRelive().intValue();
    this.enterTime = role.getEnterTime().longValue();
    this.faction = role.getFaction().intValue();
    this.drill = role.getExtendBox().getDrill();
    this.marking = role.getMarking();
  }
  
  public PlayerProfile(String sid, Integer roleId, Long id, String name, Integer icon, Integer serial, Integer level, long exp, Integer relive, Integer race, long enterTime, long lastLoginTime, Integer sex, int faction, String extend, String marking) {
    this.sid = sid;
    this.roleId = roleId;
    this.id = id;
    this.name = name;
    this.icon = icon.intValue();
    this.serial = serial.intValue();
    this.level = level.intValue();
    this.exp = exp;
    this.nextExp = FormulaHero.formulaHeroLvUpExp(level, Integer.valueOf(level.intValue() + 1), relive);
    this.relive = relive.intValue();
    this.race = race.byteValue();
    this.enterTime = enterTime;
    this.lastLoginTime = lastLoginTime;
    this.sex = sex.byteValue();
    this.faction = faction;
    if (StringUtils.isNotEmpty(extend)) {
      String content = ZipUtil.uncompress(extend);
      PlayerExtendBox box = (PlayerExtendBox)JsonUtils.string2Object(content, PlayerExtendBox.class);
      this.drill = box.getDrill();
    } 
    this.marking = marking;
  }
  
  public PlayerProfile() {}
}
