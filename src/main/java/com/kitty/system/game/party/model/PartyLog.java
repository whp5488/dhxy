package com.kitty.system.game.party.model;

import com.kitty.common.db.BaseEntity;
import com.kitty.common.thread.IdGenerator;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

@Table("party_log")
public class PartyLog extends BaseEntity<Long> {
  @Id(auto = false)
  private Long id;
  
  @Column("serial")
  private int serial;
  
  @Column("time")
  private int time;
  
  @Column("tk_id")
  private int tkId;
  
  @Column("args")
  private String[] args;
  
  public void setId(Long id) {
    this.id = id;
  }
  
  public void setSerial(int serial) {
    this.serial = serial;
  }
  
  public void setTime(int time) {
    this.time = time;
  }
  
  public void setTkId(int tkId) {
    this.tkId = tkId;
  }
  
  public void setArgs(String[] args) {
    this.args = args;
  }
  
  public Long getId() {
    return this.id;
  }
  
  public int getSerial() {
    return this.serial;
  }
  
  public int getTime() {
    return this.time;
  }
  
  public int getTkId() {
    return this.tkId;
  }
  
  public String[] getArgs() {
    return this.args;
  }
  
  public PartyLog() {}
  
  public PartyLog(int id, String[] args, int serial) {
    this((int)(System.currentTimeMillis() / 1000L), id, args, serial);
  }
  
  public PartyLog(int time, int tkId, String[] args, int serial) {
    this.id = Long.valueOf(IdGenerator.getNextId());
    this.time = time;
    this.tkId = tkId;
    this.args = args;
    this.serial = serial;
  }
}
