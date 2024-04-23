package com.kitty.system.init.domain;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Table;

@Setter
@Getter
@Table("data_lottery")
public class SysDataLottery implements Serializable {
  @Column("id")
  private int id;
  
  @Column("serial")
  private int serial;
  
  @Column("num")
  private int num;
  
  @Column("rate")
  private double rate;
  
  @Column("type")
  private byte type;

}
