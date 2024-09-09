package com.kitty.system.game.fightnew.book.vo;

import java.util.ArrayList;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.skill.domain.RoleSkill;
import java.util.List;

public class Addition
{
    private int lv;
    private int rei;
    private long experience;
    private List<RoleSkill> skills;
    protected NutMap propertyMap;
    
    public int getLv() {
        return this.lv;
    }
    
    public int getRei() {
        return this.rei;
    }
    
    public long getExperience() {
        return this.experience;
    }
    
    public List<RoleSkill> getSkills() {
        return this.skills;
    }
    
    public NutMap getPropertyMap() {
        return this.propertyMap;
    }
    
    public void setLv(final int lv) {
        this.lv = lv;
    }
    
    public void setRei(final int rei) {
        this.rei = rei;
    }
    
    public void setExperience(final long experience) {
        this.experience = experience;
    }
    
    public void setSkills(final List<RoleSkill> skills) {
        this.skills = skills;
    }
    
    public void setPropertyMap(final NutMap propertyMap) {
        this.propertyMap = propertyMap;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Addition)) {
            return false;
        }
        Addition other = (Addition)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getLv() != other.getLv()) {
            return false;
        }
        if (this.getRei() != other.getRei()) {
            return false;
        }
        if (this.getExperience() != other.getExperience()) {
            return false;
        }
        Object this$skills = this.getSkills();
        Object other$skills = other.getSkills();
        if (!((this$skills != null) ? this$skills.equals(other$skills) : (other$skills == null))) {
            return false;
        }
        Object this$propertyMap = this.getPropertyMap();
        Object other$propertyMap = other.getPropertyMap();
        return (this$propertyMap != null) ? this$propertyMap.equals(other$propertyMap) : (other$propertyMap == null);
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof Addition;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getLv();
        result = result * 59 + this.getRei();
        long $experience = this.getExperience();
        result = result * 59 + (int)($experience >>> 32 ^ $experience);
        Object $skills = this.getSkills();
        result = result * 59 + (($skills == null) ? 43 : $skills.hashCode());
        Object $propertyMap = this.getPropertyMap();
        result = result * 59 + (($propertyMap == null) ? 43 : $propertyMap.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "Addition(lv=" + this.getLv() + ", rei=" + this.getRei() + ", experience=" + this.getExperience() + ", skills=" + this.getSkills() + ", propertyMap=" + this.getPropertyMap() + ")";
    }
    
    public Addition(final int lv, final int rei, final long experience, final List<RoleSkill> skills, final NutMap propertyMap) {
        this.lv = 0;
        this.rei = 0;
        this.experience = 0L;
        this.skills = new ArrayList<>();
        this.propertyMap = new NutMap();
        this.lv = lv;
        this.rei = rei;
        this.experience = experience;
        this.skills = skills;
        this.propertyMap = propertyMap;
    }
    
    public Addition() {
        this.lv = 0;
        this.rei = 0;
        this.experience = 0L;
        this.skills = new ArrayList<>();
        this.propertyMap = new NutMap();
    }
}
