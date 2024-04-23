package com.kitty.system.init.domain;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Default;
import org.nutz.dao.entity.annotation.Table;

@Setter
@Getter
@Table("data_acsry_suit")
public class SysDataAcsrySuit {
  @Column("dist")
  private String dist;
  
  @Column("contain")
  @Default("[]")
  private List<List<Integer>> containList;
  
  @Column("name")
  private String name;

  @Column("grade")
  private int grade = 1;
  
  @Column("skill")
  @Default("[]")
  private List<Integer> skills;

}
