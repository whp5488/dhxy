package com.kitty.system.game.fightnew.messages.vo;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
public class SkillCool implements Serializable {
  private int objId;
  
  private int skillId;
  
  private int round;

}
