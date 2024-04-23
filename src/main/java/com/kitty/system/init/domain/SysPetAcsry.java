package com.kitty.system.init.domain;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Default;
import org.nutz.dao.entity.annotation.Table;

@Setter
@Getter
@Table("data_pet_acsry")
public class SysPetAcsry {
  @Column("serial")
  private int serial;
  
  @Column("name")
  private String name;
  
  @Column("adv_attr_lib")
  @Default("[]")
  private List<Integer> baseAttr;
  
  @Column("attr_lib")
  @Default("[]")
  private List<String> attrLib;

  @Column("base_score")
  private int baseScore = 0;

  @Column("grade")
  private byte grade = 0;
  
  @Column("pos")
  private int pos;

  @Column("max_endure")
  private int maxEndure = 1000;

  @Column("need_grade")
  private int needGrade = 0;

  @Column("need_rei")
  private int needRei = 0;
  
  @Column("type")
  private int type;

}
