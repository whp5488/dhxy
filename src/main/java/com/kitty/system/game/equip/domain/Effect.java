package com.kitty.system.game.equip.domain;

import com.kitty.common.db.BaseEntity;
import com.kitty.common.db.Db4CommonService;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

@Table("effect")
public class Effect extends BaseEntity<Long> {
  @Id(auto = false)
  private Long id;
  
  @Column("r_id")
  private long rId;
  
  @Column("serial")
  private int serial;
  
  @Column("state")
  private byte state;
  
  @Column("time")
  private int time;
  
  public void setId(Long id) {
    this.id = id;
  }
  
  public void setRId(long rId) {
    this.rId = rId;
  }
  
  public void setSerial(int serial) {
    this.serial = serial;
  }
  
  public void setState(byte state) {
    this.state = state;
  }
  
  public void setTime(int time) {
    this.time = time;
  }
  
  public void setPart(byte part) {
    this.part = part;
  }
  
  public Long getId() {
    return this.id;
  }
  
  public long getRId() {
    return this.rId;
  }
  
  public int getSerial() {
    return this.serial;
  }
  
  public byte getState() {
    return this.state;
  }
  
  public int getTime() {
    return this.time;
  }
  
  private byte part = 0;
  
  public byte getPart() {
    return this.part;
  }
  
  public Effect(Long id, long rId, int serial, byte state, int time, byte part) {
    this.id = id;
    this.rId = rId;
    this.serial = serial;
    this.state = state;
    this.time = time;
    this.part = part;
  }
  
  public void save() {
    Db4CommonService.getInstance().add2Queue(this);
  }
  
  public Effect() {}
}
