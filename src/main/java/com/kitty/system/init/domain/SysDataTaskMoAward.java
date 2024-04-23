package com.kitty.system.init.domain;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Table;

@Setter
@Getter
@Table("data_task_mo_award")
public class SysDataTaskMoAward implements Serializable {
  private static final long serialVersionUID = 1L;
  
  @Comment("道具编号")
  @Column("serial")
  private int serial;
  
  @Comment("男人")
  @Column("ren_male")
  private Integer renMale;
  
  @Comment("女人")
  @Column("ren_female")
  private Integer renFemale;
  
  @Comment("男仙")
  @Column("xian_male")
  private Integer xianMale;
  
  @Comment("女仙")
  @Column("xian_female")
  private Integer xianFemale;
  
  @Comment("男魔")
  @Column("mo_male")
  private Integer moMale;
  
  @Comment("女魔")
  @Column("mo_female")
  private Integer moFemale;
  
  @Comment("男鬼")
  @Column("gui_male")
  private Integer guiMale;
  
  @Comment("女鬼")
  @Column("gui_female")
  private Integer guiFemale;
  
  @Comment("男龙")
  @Column("long_male")
  private Integer longMale;
  
  @Comment("女龙")
  @Column("long_female")
  private Integer longFemale;
  
  @Comment("副本")
  @Column("dungeon")
  private int dungeon;
}
