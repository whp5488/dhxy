package com.kitty.system.init.domain;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Table;

@Setter
@Getter
@Table("data_rb_attr")
public class SysRbAttr implements Serializable {
  private static final long serialVersionUID = 1L;
  
  @Comment("主键")
  @Column("id")
  private Integer id;
  
  @Comment("等级经验")
  @Column("lv_limit")
  private Integer lvLimit;
  
  @Comment("初始技能经验")
  @Column("init_skill_exp")
  private Integer initSkillExp;
  
  @Comment("技能最大技能经验")
  @Column("max_skill_exp")
  private Integer maxSkillExp;
  
  @Comment("转生后初始等级")
  @Column("init_lv")
  private Integer initLv;
  
  @Comment("宠物转生后初始等级")
  @Column("pet_init_lv")
  private Integer petInitLv;
  
  @Comment("携带宠物上限")
  @Column("pet_num")
  private Integer petNum;

}
