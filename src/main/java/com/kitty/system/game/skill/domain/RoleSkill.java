package com.kitty.system.game.skill.domain;

import com.kitty.system.game.vo.FieldByte;
import lombok.Getter;
import lombok.Setter;

import java.beans.ConstructorProperties;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class RoleSkill implements Serializable {
  private Integer skillId;

  @ConstructorProperties({"skillId", "type", "proficient", "maxProficient", "fightType", "effects"})
  public RoleSkill(Integer skillId, Integer type, Integer proficient, Integer maxProficient, Byte fightType, List<FieldByte> effects) {
    this.skillId = skillId;
    this.type = type;
    this.proficient = proficient;
    this.maxProficient = maxProficient;
    this.fightType = fightType;
    this.effects = effects;
  }

  private Integer type = -1;
  
  private Integer proficient;
  
  private Integer maxProficient;
  
  private Byte fightType;

  private List<FieldByte> effects = new ArrayList<>();

  public RoleSkill(Integer skillId, Integer type, Integer proficient, Integer maxProficient) {
    this.skillId = skillId;
    this.type = type;
    this.proficient = proficient;
    this.maxProficient = maxProficient;
  }
  
  public RoleSkill() {}
}
