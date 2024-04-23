package com.kitty.system.init.domain;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Default;
import org.nutz.dao.entity.annotation.Table;

@Setter
@Getter
@Table("s_skill")
public class SysSkill implements Serializable {
  private static final long serialVersionUID = 1L;
  
  @Comment("主键")
  @Column("id")
  private Integer id;
  
  @Comment("技能编号")
  @Column("serial")
  private Integer serial;
  
  @Comment("名字")
  @Column("name")
  private String name;
  
  @Comment("图标")
  @Column("icon")
  private int icon;
  
  @Comment("技能派系")
  @Column("attr")
  private Integer attr;
  
  @Comment("技能等阶：4.单体 5.群体")
  @Column("step")
  private Integer step;
  
  @Comment("种族")
  @Column("race")
  private Integer race;
  
  @Comment("性别")
  @Column("sex")
  private Integer sex;
  
  @Comment("是否主动技能（1主动，0被动）")
  @Column("initiative")
  private Integer initiative;
  
  @Comment("目标类型")
  @Column("target_type")
  @Default("[]")
  private List<Integer> targetType;
  
  @Comment("技能类型：0英雄")
  @Column("type")
  private Integer type;
  
  @Comment("命中率")
  @Column("hit_ratio")
  private double hitRatio;
  
  @Comment("系数")
  @Column("power")
  @Default("[]")
  private double[] power;
  
  @Comment("覆盖技能编号（本技能被此字段技能覆盖），可以是多个，用逗号隔开")
  @Column("cover_skill_serials")
  private String coverSkillSerials;
  
  @Comment("位置")
  @Column("pos")
  private Integer pos;

}
