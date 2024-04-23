package com.kitty.record.domain;

import com.kitty.record.FightRecordThread;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Setter
@Getter
public class FightRecord implements Serializable {
  private Long id;
  
  private Long recordId;
  
  private byte type;
  
  private int fightId;
  
  private short round;
  
  private int module;
  
  private long createTime;
  
  private List<Long> roleIds;
  
  private String marking;
  
  private String sendRoles;

  public void save() {
    FightRecordThread.getInstance().add2Queue(this);
  }
}
