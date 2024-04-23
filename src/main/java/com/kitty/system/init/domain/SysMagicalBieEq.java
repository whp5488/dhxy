package com.kitty.system.init.domain;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Default;
import org.nutz.dao.entity.annotation.Table;

@Setter
@Getter
@Table("magical_bie_eq")
public class SysMagicalBieEq implements Serializable {
  @Column("id")
  private int id;
  
  @Column("name")
  private String name;
  
  @Column("serial")
  private int serial;
  
  @Column("base_attr")
  private String baseAttr;
  
  @Column("base_score")
  private int baseScore;
  
  @Column("pos")
  private int pos;
  
  @Column("max_embed_gem_cnt")
  private int maxEmbedGemCnt;
  
  @Column("max_endure")
  private int maxEndure;
  
  @Column("need_gg")
  private int needGg;
  
  @Column("need_lx")
  private int needLx;
  
  @Column("need_ll")
  private int needLl;
  
  @Column("need_mj")
  private int needMj;
  
  @Column("need_grade")
  private int needGrade;
  
  @Column("need_rei")
  private int needRei;
  
  @Column("owner_role")
  @Default("[]")
  private List<Integer> ownerRole;
  
  @Column("race")
  private int race;
  
  @Column("sex")
  private int sex;
  
  @Column("shape")
  private int shape;
  
  @Column("can_merge")
  private int canMerge;
  
  @Column("rnd_range")
  @Default("[]")
  private double[] rndRange;
}
