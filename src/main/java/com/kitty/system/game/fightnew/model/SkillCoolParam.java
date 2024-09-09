package com.kitty.system.game.fightnew.model;

import com.kitty.system.game.fightnew.messages.vo.SkillCool;
import java.util.List;

public class SkillCoolParam
{
    private int roleId;
    private List<SkillCool> skillCools;
    
    public void setRoleId(final int roleId) {
        this.roleId = roleId;
    }
    
    public void setSkillCools(final List<SkillCool> skillCools) {
        this.skillCools = skillCools;
    }
    
    public int getRoleId() {
        return this.roleId;
    }
    
    public List<SkillCool> getSkillCools() {
        return this.skillCools;
    }
    
    public SkillCoolParam(final int roleId, final List<SkillCool> skillCools) {
        this.roleId = roleId;
        this.skillCools = skillCools;
    }
    
    public SkillCoolParam() {
    }
}
