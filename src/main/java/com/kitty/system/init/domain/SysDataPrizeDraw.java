package com.kitty.system.init.domain;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Table;

@Setter
@Getter
@Table("data_prize_draw")
public class SysDataPrizeDraw implements Serializable {
  private static final long serialVersionUID = 1L;
  
  @Column("id")
  private int id;
  
  @Column("serial")
  private int serial;
  
  @Column("num")
  private int num;
  
  @Column("rate")
  private double rate;
  
  @Column("type")
  private int type;

  public SysDataPrizeDraw() {}
  
  public SysDataPrizeDraw(int serial, int num, int type) {
    this.serial = serial;
    this.num = num;
    this.type = type;
  }
  
  public SysDataPrizeDraw(int id, int serial, int num, double rate, int type) {
    this.id = id;
    this.serial = serial;
    this.num = num;
    this.rate = rate;
    this.type = type;
  }
}
