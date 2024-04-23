package com.kitty.system.init.domain;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Table;

@Setter
@Getter
@Table("t_data_stall_menu")
public class SysDataStallMenu implements Serializable {
  @Comment("菜单编号")
  @Column("serial")
  private Integer serial;
  
  @Comment("父菜单编号")
  @Column("parent_serial")
  private Integer parentSerial;
  
  @Comment("祖级")
  @Column("ancestors")
  private Integer ancestors;
  
  @Comment("0.正常，1.停用")
  @Column("status")
  private Integer categoryName;

}
