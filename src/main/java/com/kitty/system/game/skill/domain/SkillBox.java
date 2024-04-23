package com.kitty.system.game.skill.domain;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Setter
@Getter
public class SkillBox implements Serializable {

  private Map<Integer, RoleSkill> skills = new HashMap<>();
}
