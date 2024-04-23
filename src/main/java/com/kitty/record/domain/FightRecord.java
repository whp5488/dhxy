package com.kitty.record.domain;

import com.kitty.record.FightRecordThread;
import java.io.Serializable;
import java.util.List;

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
  
  public void setId(Long id) {
    this.id = id;
  }
  
  public void setRecordId(Long recordId) {
    this.recordId = recordId;
  }
  
  public void setType(byte type) {
    this.type = type;
  }
  
  public void setFightId(int fightId) {
    this.fightId = fightId;
  }
  
  public void setRound(short round) {
    this.round = round;
  }
  
  public void setModule(int module) {
    this.module = module;
  }
  
  public void setCreateTime(long createTime) {
    this.createTime = createTime;
  }
  
  public void setRoleIds(List<Long> roleIds) {
    this.roleIds = roleIds;
  }
  
  public void setMarking(String marking) {
    this.marking = marking;
  }
  
  public void setSendRoles(String sendRoles) {
    this.sendRoles = sendRoles;
  }
  
  public Long getId() {
    return this.id;
  }
  
  public Long getRecordId() {
    return this.recordId;
  }
  
  public byte getType() {
    return this.type;
  }
  
  public int getFightId() {
    return this.fightId;
  }
  
  public short getRound() {
    return this.round;
  }
  
  public int getModule() {
    return this.module;
  }
  
  public long getCreateTime() {
    return this.createTime;
  }
  
  public List<Long> getRoleIds() {
    return this.roleIds;
  }
  
  public String getMarking() {
    return this.marking;
  }
  
  public String getSendRoles() {
    return this.sendRoles;
  }
  
  public void save() {
    FightRecordThread.getInstance().add2Queue(this);
  }
}
