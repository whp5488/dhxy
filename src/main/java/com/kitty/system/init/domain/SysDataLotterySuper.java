package com.kitty.system.init.domain;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Table;

@Setter
@Getter
@Table("data_lottery_super")
public class SysDataLotterySuper implements Serializable {
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
  private byte type;
  
  @Column("is_notice")
  private byte isNotice;
}
