package com.kitty.system.game.market.model;

import com.kitty.common.db.BaseEntity;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

@Table("t_materials_record")
public class MaterialsRecord extends BaseEntity<Long> {
  @Id(auto = false)
  @Column("id")
  private Long id;
  
  public void setId(Long id) {
    this.id = id;
  }
  
  public void setTime(int time) {
    this.time = time;
  }
  
  public void setSerial(int serial) {
    this.serial = serial;
  }
  
  public void setPublish(long publish) {
    this.publish = publish;
  }
  
  public void setConsume(long consume) {
    this.consume = consume;
  }
  
  public Long getId() {
    return this.id;
  }
  
  @Column("time")
  private int time = 0;
  
  public int getTime() {
    return this.time;
  }
  
  @Column("serial")
  private int serial = 0;
  
  public int getSerial() {
    return this.serial;
  }
  
  @Column("publish")
  private long publish = 0L;
  
  public long getPublish() {
    return this.publish;
  }
  
  @Column("consume")
  private long consume = 0L;
  
  public long getConsume() {
    return this.consume;
  }
}
