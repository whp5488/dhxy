package com.kitty.system.init.domain;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Table;

@Setter
@Getter
@Table("data_magic_box")
public class SysDataMagicBox implements Serializable {
  @Column("identify")
  private int identify;
  
  @Column("affiliation")
  private int affiliation;
  
  @Column("serial")
  private int serial;
  
  @Column("num")
  private short num;
  
  @Column("rate")
  private double rate;
  
  @Column("type")
  private byte type;
  
  @Column("classify")
  private byte classify;
  
  @Column("certainly")
  private byte certainly;
  
  @Column("is_notice")
  private byte isNotice;

  @Column("bd")
  private int bd = 0;

}
