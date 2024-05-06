package com.kitty.system.game.player.bean;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Setter
@Getter
public class PkInfoBox implements Serializable {
  private boolean redName;
  
  private int incense;
  
  private int conceal;
  
  private int redTime;
  
  private int prisonTime;

  private Map<Long, PkRecord> killRoles = new HashMap<>();

  private Map<Long, PkRecord> beKillRoles = new HashMap<>();

}
