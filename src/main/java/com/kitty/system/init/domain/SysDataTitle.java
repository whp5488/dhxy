package com.kitty.system.init.domain;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Table;

@Setter
@Getter
@Table("data_title")
public class SysDataTitle implements Serializable {
  @Comment("编号")
  @Column("serial")
  private int serial;
  
  @Comment("名称")
  @Column("name")
  private String name;
  
  @Comment("过期（秒）-1.自定义 0.为永久")
  @Column("expire")
  private int expire;

}
