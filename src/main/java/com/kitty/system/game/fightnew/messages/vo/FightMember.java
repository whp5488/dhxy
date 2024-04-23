package com.kitty.system.game.fightnew.messages.vo;

import com.kitty.system.common.enter.FiedValue;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class FightMember implements Serializable {
  private int id;
  
  private int serial;
  
  private int host;
  
  private int leader;
  
  private int position;
  
  private int icon;

  private int weaponIcon = 0;

  private List<Integer> light_effect = new ArrayList<>();

  private List<Integer> special_icon = new ArrayList<>();
  
  private List<FiedValue> list;
  
  private List<Integer> statusList;

}
