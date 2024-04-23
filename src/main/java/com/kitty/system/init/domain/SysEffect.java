package com.kitty.system.init.domain;

import lombok.Getter;
import lombok.Setter;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Table;

@Setter
@Getter
@Table("data_effect")
public class SysEffect {
  @Column("serial")
  private int serial;
  
  @Column("name")
  private String name;
  
  @Column("part")
  private byte part;
  
  @Column("icon")
  private int icon;

}
