package com.kitty.system.init.domain;

import lombok.Getter;
import lombok.Setter;
import org.nutz.dao.entity.annotation.ColDefine;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Table;
import org.nutz.lang.util.NutMap;

@Setter
@Getter
@Table("data_card")
public class SysDataCard {
  @Column("serial")
  private int serial;
  
  @Column("name")
  private String name;
  
  @Column("need_five_grade")
  private int needFiveGrade;
  
  @Column("need_owner_grade")
  private int needOwnerGrade;
  
  @Column("expire")
  private int canMerge;
  
  @Column("rarity")
  private int rarity;
  
  @Column("base_attr")
  @ColDefine(customType = "text")
  private NutMap baseAttr;
  
  @Column("five_attr")
  private NutMap fiveAttr;
  
  @Column("factor")
  private double factor;
  
  @Column("morph")
  private int morph;
  
  @Column("reward_rarity")
  private int rewardRarity;
  
  @Column("expire")
  private int expire;
}
