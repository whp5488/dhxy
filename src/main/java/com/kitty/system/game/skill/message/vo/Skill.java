package com.kitty.system.game.skill.message.vo;

import com.kitty.system.game.skill.domain.RoleSkill;
import lombok.Getter;
import lombok.Setter;

import java.beans.ConstructorProperties;
import java.io.Serializable;

@Setter
@Getter
public class Skill implements Serializable {
  private Integer skillId;
  
  @ConstructorProperties({"skillId", "type", "proficient", "maxProficient"})
  public Skill(Integer skillId, Integer type, Integer proficient, Integer maxProficient) {
    this.skillId = skillId;
    this.type = type;
    this.proficient = proficient;
    this.maxProficient = maxProficient;
  }

  private Integer type = Integer.valueOf(-1);
  
  private Integer proficient;
  
  private Integer maxProficient;

  public Skill(RoleSkill roleSkill) {
    this.skillId = roleSkill.getSkillId();
    this.type = roleSkill.getType();
    this.proficient = roleSkill.getProficient();
    this.maxProficient = roleSkill.getMaxProficient();
  }
  
  public Skill() {}
}
