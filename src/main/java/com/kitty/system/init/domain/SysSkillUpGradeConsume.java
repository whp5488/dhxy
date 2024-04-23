package com.kitty.system.init.domain;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Table;

@Setter
@Getter
@Table("data_skill_up_grade_consume")
public class SysSkillUpGradeConsume implements Serializable {
  @Comment("熟练度等级")
  @Column("grade")
  private Integer grade;
  
  @Comment("单目标技能耗费")
  @Column("alone")
  private Integer alone;
  
  @Comment("多目标技能耗费")
  @Column("more")
  private Integer more;
}
