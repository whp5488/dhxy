package com.kitty.system.game.team.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;

@Setter
@Getter
@MessageMeta(module = 4156)
public class ReqFoundJoin extends Message {
  private int roleId;
  
  private String name;
  
  private String type;
  
  private String target;
  
  private String msg;
  
  private int[] raceLimit;
  
  private int sexLimit;
  
  private int minLevel;
  
  private int minRelive;
  
  private int maxLevel;
  
  private int maxRelive;

  public ReqFoundJoin() {}
  
  public ReqFoundJoin(String target, String msg, int[] raceLimit, int sexLimit, int minLevel, int minRelive, int maxLevel, int maxRelive) {
    this.target = target;
    this.msg = msg;
    this.raceLimit = raceLimit;
    this.sexLimit = sexLimit;
    this.minLevel = minLevel;
    this.minRelive = minRelive;
    this.maxLevel = maxLevel;
    this.maxRelive = maxRelive;
  }
}
