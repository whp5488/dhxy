package com.kitty.system.game.party.model.vo;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PartyMemberObject {
  private int roleId;
  
  private String name;
  
  private byte rei;
  
  private short level;
  
  private int roleSerial;
  
  private byte job;
  
  private int joinTime;

  private int weekBangBtl = 0;

  private int thisBangBtl = 0;

  private int weekEcType = 0;

  private int partyTask = 0;

  private String contribute = "0";
  
  private int lastOnlineTime;

  private byte onLineStatus = 0;

}
