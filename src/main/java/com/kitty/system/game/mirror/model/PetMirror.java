
package com.kitty.system.game.mirror.model;

import com.kitty.system.game.pet.domain.vo.PetSkill;
import java.util.List;

public class PetMirror
{
    private String name;
    private int serial;
    private int level;
    private int defense;
    private int speed;
    private int magPower;
    private int phyPower;
    private int maxLife;
    private int maxMana;
    private List<PetSkill> skillIds;
    private int icon;
    private long intimacy;
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public void setSerial(final int serial) {
        this.serial = serial;
    }
    
    public void setLevel(final int level) {
        this.level = level;
    }
    
    public void setDefense(final int defense) {
        this.defense = defense;
    }
    
    public void setSpeed(final int speed) {
        this.speed = speed;
    }
    
    public void setMagPower(final int magPower) {
        this.magPower = magPower;
    }
    
    public void setPhyPower(final int phyPower) {
        this.phyPower = phyPower;
    }
    
    public void setMaxLife(final int maxLife) {
        this.maxLife = maxLife;
    }
    
    public void setMaxMana(final int maxMana) {
        this.maxMana = maxMana;
    }
    
    public void setSkillIds(final List<PetSkill> skillIds) {
        this.skillIds = skillIds;
    }
    
    public void setIcon(final int icon) {
        this.icon = icon;
    }
    
    public void setIntimacy(final long intimacy) {
        this.intimacy = intimacy;
    }
    
    public String getName() {
        return this.name;
    }
    
    public int getSerial() {
        return this.serial;
    }
    
    public int getLevel() {
        return this.level;
    }
    
    public int getDefense() {
        return this.defense;
    }
    
    public int getSpeed() {
        return this.speed;
    }
    
    public int getMagPower() {
        return this.magPower;
    }
    
    public int getPhyPower() {
        return this.phyPower;
    }
    
    public int getMaxLife() {
        return this.maxLife;
    }
    
    public int getMaxMana() {
        return this.maxMana;
    }
    
    public List<PetSkill> getSkillIds() {
        return this.skillIds;
    }
    
    public int getIcon() {
        return this.icon;
    }
    
    public long getIntimacy() {
        return this.intimacy;
    }
}
