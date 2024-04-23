package com.kitty.system.init.domain;

import java.beans.ConstructorProperties;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Default;
import org.nutz.dao.entity.annotation.Table;

@Setter
@Getter
@Table("data_acsry_skill")
public class SysDataAcsrySkill {
  @Column("serial")
  private int serial;
  
  @Column("name")
  private int name;
  
  @Column("serial")
  private int initiative;
  
  @Column("trigger_type")
  @Default("[]")
  private List<String> triggerType;
  
  @Column("forthwith_type")
  @Default("[]")
  private List<Integer> forthwithType;

  public SysDataAcsrySkill() {}
  
  @ConstructorProperties({"serial", "name", "initiative", "triggerType", "forthwithType"})
  public SysDataAcsrySkill(int serial, int name, int initiative, List<String> triggerType, List<Integer> forthwithType) {
    this.serial = serial;
    this.name = name;
    this.initiative = initiative;
    this.triggerType = triggerType;
    this.forthwithType = forthwithType;
  }

}
