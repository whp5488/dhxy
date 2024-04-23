package com.kitty.system.game.team.domain;

import com.kitty.system.common.enter.FiedValue;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class TeamMember {
  private Integer roleId;

  private Integer suit_icon = 0;

  private Integer weaponIcon = 0;

  private byte gem = 0;
  
  private List<FiedValue> list;

  private int masquerade = 0;

  private List<Integer> light_effect = new ArrayList<>();

  private List<Integer> special_icon = new ArrayList<>();

  private List<FiedValue> pet = new ArrayList<>();

  private int status = 1;

}
