package com.kitty.system.init.domain;

import java.beans.ConstructorProperties;

import lombok.Getter;
import lombok.Setter;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Table;

@Setter
@Getter
@Table("data_acsry_suit_color")
public class SysDataAcsrySuitColor {
  @Column("id")
  private int id;
  
  @Column("skill_serial")
  private int skillSerial;
  
  @Column("suit_dist")
  private String suitDist;
  
  @Column("suit_name")
  private String suitName;
  
  @Column("color_id")
  private byte colorId;

  public SysDataAcsrySuitColor() {}
  
  @ConstructorProperties({"id", "skillSerial", "suitDist", "suitName", "colorId"})
  public SysDataAcsrySuitColor(int id, int skillSerial, String suitDist, String suitName, byte colorId) {
    this.id = id;
    this.skillSerial = skillSerial;
    this.suitDist = suitDist;
    this.suitName = suitName;
    this.colorId = colorId;
  }

}
