package com.kitty.system.game.vo;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
public class EasyNpc implements Serializable {
  private int id;
  
  private short m;
  
  private short x;
  
  private short y;

  private short s = 1;
  
  private String n;

  private byte t = 1;

  public EasyNpc(int id, short m, short x, short y, short s, String n) {
    this.id = id;
    this.m = m;
    this.x = x;
    this.y = y;
    this.s = s;
    this.n = n;
  }
  
  public EasyNpc(Npc npc, byte t) {
    this.id = npc.getId();
    this.m = (short)npc.getMap();
    this.x = (short)npc.getX();
    this.y = (short)npc.getY();
    this.s = (short)npc.getIcon();
    this.n = npc.getName();
    this.t = t;
  }
  
  public EasyNpc() {}
}
