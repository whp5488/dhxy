package com.kitty.system.game.fightnew.model;

import java.io.Serializable;

public class AutoFightSkillParam implements Serializable
{
    private int objId;
    private int action;
    private int skillId;
    private int born;
    
    public void setObjId(final int objId) {
        this.objId = objId;
    }
    
    public void setAction(final int action) {
        this.action = action;
    }
    
    public void setSkillId(final int skillId) {
        this.skillId = skillId;
    }
    
    public void setBorn(final int born) {
        this.born = born;
    }
    
    public int getObjId() {
        return this.objId;
    }
    
    public int getAction() {
        return this.action;
    }
    
    public int getSkillId() {
        return this.skillId;
    }
    
    public int getBorn() {
        return this.born;
    }
    
    public AutoFightSkillParam(final int objId, final int action, final int skillId, final int born) {
        this.skillId = -1;
        this.born = 0;
        this.objId = objId;
        this.action = action;
        this.skillId = skillId;
        this.born = born;
    }
    
    public AutoFightSkillParam() {
        this.skillId = -1;
        this.born = 0;
    }
}
