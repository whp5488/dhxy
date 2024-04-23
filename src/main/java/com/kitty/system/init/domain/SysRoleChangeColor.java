package com.kitty.system.init.domain;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Table;

@Setter
@Getter
@Table("role_change_color")
public class SysRoleChangeColor implements Serializable {
  private static final long serialVersionUID = 1L;
  
  @Comment("主键")
  @Column("id")
  private Integer id;
  
  @Comment("角色外观编号")
  @Column("serial")
  private Integer serial;
  
  @Comment("部位-1")
  @Column("color_one")
  private String colorOne;
  
  @Comment("部位-2")
  @Column("color_two")
  private String colorTwo;

}
