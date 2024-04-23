package com.kitty.system.init.domain;

import lombok.Getter;
import lombok.Setter;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Table;

@Setter
@Getter
@Table("data_furniture")
public class SysDataFurniture {
  @Column("serial")
  private int serial;
  
  @Column("name")
  private String name;

  @Column("classify")
  private int classify = 0;
  
  @Column("house")
  private int house;
  
  @Column("type")
  private int type;
  
  @Column("price")
  private long price;

}
