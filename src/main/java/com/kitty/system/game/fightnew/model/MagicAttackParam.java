
package com.kitty.system.game.fightnew.model;

import java.beans.ConstructorProperties;
import com.kitty.system.game.fightnew.FightObject;
import java.util.List;
import java.io.Serializable;

public class MagicAttackParam implements Serializable
{
    private boolean isDodge;
    private List<Integer> positiveStatus;
    private FightObject target;
    private int hurt;
    private List<Integer> clearStatusList;
    private int targetReduceMp;
    private FightObject sideTarget;
    
    public void setDodge(final boolean isDodge) {
        this.isDodge = isDodge;
    }
    
    public void setPositiveStatus(final List<Integer> positiveStatus) {
        this.positiveStatus = positiveStatus;
    }
    
    public void setTarget(final FightObject target) {
        this.target = target;
    }
    
    public void setHurt(final int hurt) {
        this.hurt = hurt;
    }
    
    public void setClearStatusList(final List<Integer> clearStatusList) {
        this.clearStatusList = clearStatusList;
    }
    
    public void setTargetReduceMp(final int targetReduceMp) {
        this.targetReduceMp = targetReduceMp;
    }
    
    public void setSideTarget(final FightObject sideTarget) {
        this.sideTarget = sideTarget;
    }
    
    public boolean isDodge() {
        return this.isDodge;
    }
    
    public List<Integer> getPositiveStatus() {
        return this.positiveStatus;
    }
    
    public FightObject getTarget() {
        return this.target;
    }
    
    public int getHurt() {
        return this.hurt;
    }
    
    public List<Integer> getClearStatusList() {
        return this.clearStatusList;
    }
    
    public int getTargetReduceMp() {
        return this.targetReduceMp;
    }
    
    public FightObject getSideTarget() {
        return this.sideTarget;
    }
    
    @ConstructorProperties({ "isDodge", "positiveStatus", "target", "hurt", "clearStatusList", "targetReduceMp", "sideTarget" })
    public MagicAttackParam(final boolean isDodge, final List<Integer> positiveStatus, final FightObject target, final int hurt, final List<Integer> clearStatusList, final int targetReduceMp, final FightObject sideTarget) {
        this.isDodge = isDodge;
        this.positiveStatus = positiveStatus;
        this.target = target;
        this.hurt = hurt;
        this.clearStatusList = clearStatusList;
        this.targetReduceMp = targetReduceMp;
        this.sideTarget = sideTarget;
    }
    
    public MagicAttackParam() {
    }
    
    @Override
    public String toString() {
        return "MagicAttackParam(isDodge=" + this.isDodge() + ", positiveStatus=" + this.getPositiveStatus() + ", target=" + this.getTarget() + ", hurt=" + this.getHurt() + ", clearStatusList=" + this.getClearStatusList() + ", targetReduceMp=" + this.getTargetReduceMp() + ", sideTarget=" + this.getSideTarget() + ")";
    }
}
