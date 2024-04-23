package com.kitty.system.init.domain;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Table;

@Setter
@Getter
@Table("data_upgrade")
public class SysUpgrade implements Serializable {
  private static final long serialVersionUID = 1L;
  
  @Comment("编号")
  @Column("id")
  private Integer id;
  
  @Comment("转生")
  @Column("zs")
  private Integer zs;
  
  @Comment("等级")
  @Column("lv")
  private Integer lv;
  
  @Comment("奖励")
  @Column("reward")
  private String reward;


}
