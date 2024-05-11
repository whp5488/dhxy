
package com.kitty.system.game.activity.model.user.wars.vo;

import java.io.Serializable;

public class GangWarsInfo implements Serializable
{
    private short gateLife;
    private byte stance;
    private int visible;
    private int grade;
    private int enemy;
    private byte combatResults;
    
    public GangWarsInfo(final byte stance, final int visible, final int grade, final int enemy) {
        this.gateLife = 5000;
        this.grade = 1;
        this.combatResults = 3;
        this.stance = stance;
        this.visible = visible;
        this.grade = grade;
        this.enemy = enemy;
    }
    
    public GangWarsInfo(final short gateLife, final byte stance, final int visible, final int grade, final int enemy) {
        this.gateLife = 5000;
        this.grade = 1;
        this.combatResults = 3;
        this.gateLife = gateLife;
        this.stance = stance;
        this.visible = visible;
        this.grade = grade;
        this.enemy = enemy;
    }
    
    public short getGateLife() {
        return this.gateLife;
    }
    
    public byte getStance() {
        return this.stance;
    }
    
    public int getVisible() {
        return this.visible;
    }
    
    public int getGrade() {
        return this.grade;
    }
    
    public int getEnemy() {
        return this.enemy;
    }
    
    public byte getCombatResults() {
        return this.combatResults;
    }
    
    public void setGateLife(final short gateLife) {
        this.gateLife = gateLife;
    }
    
    public void setStance(final byte stance) {
        this.stance = stance;
    }
    
    public void setVisible(final int visible) {
        this.visible = visible;
    }
    
    public void setGrade(final int grade) {
        this.grade = grade;
    }
    
    public void setEnemy(final int enemy) {
        this.enemy = enemy;
    }
    
    public void setCombatResults(final byte combatResults) {
        this.combatResults = combatResults;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof GangWarsInfo)) {
            return false;
        }
        final GangWarsInfo other = (GangWarsInfo)o;
        return other.canEqual(this) && this.getGateLife() == other.getGateLife() && this.getStance() == other.getStance() && this.getVisible() == other.getVisible() && this.getGrade() == other.getGrade() && this.getEnemy() == other.getEnemy() && this.getCombatResults() == other.getCombatResults();
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof GangWarsInfo;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getGateLife();
        result = result * 59 + this.getStance();
        result = result * 59 + this.getVisible();
        result = result * 59 + this.getGrade();
        result = result * 59 + this.getEnemy();
        result = result * 59 + this.getCombatResults();
        return result;
    }
    
    @Override
    public String toString() {
        return "GangWarsInfo(gateLife=" + this.getGateLife() + ", stance=" + this.getStance() + ", visible=" + this.getVisible() + ", grade=" + this.getGrade() + ", enemy=" + this.getEnemy() + ", combatResults=" + this.getCombatResults() + ")";
    }
    
    public GangWarsInfo() {
        this.gateLife = 5000;
        this.grade = 1;
        this.combatResults = 3;
    }
}
