package com.kitty.system.game.team.domain.vo;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.LinkedHashMap;

@Setter
@Getter
public class TeamRequest implements Serializable {
  private LinkedHashMap<Integer, String> map;
}
