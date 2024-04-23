package com.kitty.system.init.domain;

import lombok.Getter;
import lombok.Setter;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Table;

@Setter
@Getter
@Table("data_acsry_egg")
public class SysDataAcsryEgg {
  @Column("serial")
  private int serial;
  
  @Column("hatch_type")
  private int hatchType;
  
  @Column("name")
  private String name;

  @Column("can_merge")
  private int canMerge = 1;
  
  @Column("rarity")
  private int rarity;
  
  @Column("shape")
  private int shape;

}
