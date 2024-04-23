package com.kitty.system.game.activity.model;

import com.kitty.common.db.BaseEntity;
import com.kitty.common.db.Db4EventService;
import org.nutz.dao.entity.annotation.ColDefine;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

@Table("t_event")
public class Event extends BaseEntity<Long> {
  @Comment("主键")
  @Id(auto = false)
  @Column("id")
  private Long id;
  
  @Column("serial")
  private int serial;
  
  @Column("para")
  @ColDefine(customType = "text")
  private String para;
  
  public void setId(Long id) {
    this.id = id;
  }
  
  public void setSerial(int serial) {
    this.serial = serial;
  }
  
  public void setPara(String para) {
    this.para = para;
  }
  
  public String toString() {
    return "Event(id=" + getId() + ", serial=" + getSerial() + ", para=" + getPara() + ")";
  }
  
  public Long getId() {
    return this.id;
  }
  
  public int getSerial() {
    return this.serial;
  }
  
  public String getPara() {
    return this.para;
  }
  
  public Event() {}
  
  public Event(Long id, int serial) {
    this.id = id;
    this.serial = serial;
  }
  
  public void save() {
    Db4EventService.getInstance().add2Queue(this);
  }
}
