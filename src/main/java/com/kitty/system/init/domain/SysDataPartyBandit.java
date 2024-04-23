package com.kitty.system.init.domain;

import com.kitty.system.init.domain.vo.Param;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Default;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

@Setter
@Getter
@Table("data_party_bandit")
public class SysDataPartyBandit implements Serializable {
  @Column("id")
  @Id
  private int id;
  
  @Column("name")
  private String name;
  
  @Column("title")
  private String title;
  
  @Column("grade")
  private int grade;
  
  @Column("icon")
  private int icon;
  
  @Column("level")
  private int level;
  
  @Column("war")
  @Default("[]")
  private List<Integer> war;
  
  @Column("num")
  private long num;
  
  @Column("award")
  @Default("[]")
  private List<Param> award;

  @Column("special")
  @Default("[]")
  private List<Param> special = new ArrayList<>();

}
