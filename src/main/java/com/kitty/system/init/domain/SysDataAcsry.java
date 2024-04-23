package com.kitty.system.init.domain;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Default;
import org.nutz.dao.entity.annotation.Table;

@Setter
@Getter
@Table("data_acsry")
public class SysDataAcsry {
  @Column("serial")
  private int serial;
  
  @Column("name")
  private String name;
  
  @Column("base_attr")
  private String baseAttr;

  @Column("base_score")
  private int baseScore = 0;
  
  @Column("pos")
  private int pos;

  @Column("max_endure")
  private int maxEndure = 1000;

  @Column("need_gg")
  private int needGg = 0;

  @Column("need_lx")
  private int needLx = 0;

  @Column("need_ll")
  private int needLl = 0;

  @Column("need_mj")
  private int needMj = 0;

  @Column("need_grade")
  private int needGrade = 0;

  @Column("need_rei")
  private int needRei = 0;
  
  @Column("owner_role")
  @Default("[]")
  private List<Integer> ownerRole;
  
  @Column("sex")
  private int sex;

  @Column("rarity")
  private int rarity = 1;
  
  @Column("shape")
  private int shape;
  
  @Column("rnd_range")
  private double rndRange;
  
  @Column("basic_price")
  private long basicPrice;
  
  @Column("suite_type")
  private String suiteType;

}
