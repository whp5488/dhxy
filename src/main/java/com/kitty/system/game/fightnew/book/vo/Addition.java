package com.kitty.system.game.fightnew.book.vo;

import com.kitty.system.game.skill.domain.RoleSkill;
import java.beans.ConstructorProperties;
import java.util.ArrayList;
import java.util.List;
import org.nutz.lang.util.NutMap;

public class Addition {
  public void setLv(int lv) {
    this.lv = lv;
  }
  
  public void setRei(int rei) {
    this.rei = rei;
  }
  
  public void setExperience(long experience) {
    this.experience = experience;
  }
  
  public void setSkills(List<RoleSkill> skills) {
    this.skills = skills;
  }
  
  public void setPropertyMap(NutMap propertyMap) {
    this.propertyMap = propertyMap;
  }
  
  @ConstructorProperties({"lv", "rei", "experience", "skills", "propertyMap"})
  public Addition(int lv, int rei, long experience, List<RoleSkill> skills, NutMap propertyMap) {
    this.lv = lv;
    this.rei = rei;
    this.experience = experience;
    this.skills = skills;
    this.propertyMap = propertyMap;
  }
  
  private int lv = 0;
  
  public int getLv() {
    return this.lv;
  }
  
  private int rei = 0;
  
  public int getRei() {
    return this.rei;
  }
  
  private long experience = 0L;
  
  public long getExperience() {
    return this.experience;
  }
  
  private List<RoleSkill> skills = new ArrayList<>();
  
  public List<RoleSkill> getSkills() {
    return this.skills;
  }
  
  protected NutMap propertyMap = new NutMap();
  
  public NutMap getPropertyMap() {
    return this.propertyMap;
  }
  
  public Addition() {}
}
