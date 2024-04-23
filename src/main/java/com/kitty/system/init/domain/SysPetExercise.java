package com.kitty.system.init.domain;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Table;

@Setter
@Getter
@Table("data_pet_exercise")
public class SysPetExercise implements Serializable {
  private static final long serialVersionUID = 1L;
  
  @Comment("")
  @Column("id")
  private int id;
  
  @Comment("魂魄等级")
  @Column("soul")
  private int soul;
  
  @Comment("等级限值")
  @Column("level")
  private int level;
  
  @Comment("转生限值")
  @Column("relive")
  private int relive;
  
  @Comment("修炼经验")
  @Column("exp")
  private int exp;
}
