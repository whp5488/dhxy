package com.kitty.system.game.fightnew.model;

import com.kitty.system.game.fightnew.messages.vo.SkillCool;
import java.beans.ConstructorProperties;
import java.util.List;

public class SkillCoolParam {
  private int roleId;
  
  private List<SkillCool> skillCools;
  
  public void setRoleId(int roleId) {
    this.roleId = roleId;
  }
  
  public void setSkillCools(List<SkillCool> skillCools) {
    this.skillCools = skillCools;
  }
  
  @ConstructorProperties({"roleId", "skillCools"})
  public SkillCoolParam(int roleId, List<SkillCool> skillCools) {
    this.roleId = roleId;
    this.skillCools = skillCools;
  }
  
  public SkillCoolParam() {}
  
  public int getRoleId() {
    return this.roleId;
  }
  
  public List<SkillCool> getSkillCools() {
    return this.skillCools;
  }
}
