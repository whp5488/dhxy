package com.kitty.system.init.domain;

import java.beans.ConstructorProperties;
import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Table;

@Setter
@Getter
@Table("data_npc_type_info")
public class SysNpcTypeInfo implements Serializable {
  private static final long serialVersionUID = 1L;
  
  @Comment("主键")
  @Column("id")
  private Integer id;
  
  @Comment("按钮名称")
  @Column("name")
  private String name;
  
  @Comment("转生限值")
  @Column("zs")
  private Integer zs;
  
  @Comment("等级限值")
  @Column("lv")
  private Integer lv;
  
  public SysNpcTypeInfo() {}
  
  @ConstructorProperties({"id", "name", "zs", "lv"})
  public SysNpcTypeInfo(Integer id, String name, Integer zs, Integer lv) {
    this.id = id;
    this.name = name;
    this.zs = zs;
    this.lv = lv;
  }

}
