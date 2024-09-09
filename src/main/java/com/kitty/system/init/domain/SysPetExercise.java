package com.kitty.system.init.domain;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Table;
import java.io.Serializable;

@Table("data_pet_exercise")
public class SysPetExercise implements Serializable
{
    private static final long serialVersionUID = 1L;
    @Comment("")
    @Column("id")
    private int id;
    @Comment("魂魄等级")
    @Column("soul")
    private int soul;
    @Comment("等级限值")
    @Column("level")
    private int level;
    @Comment("转生限值")
    @Column("relive")
    private int relive;
    @Comment("修炼经验")
    @Column("exp")
    private int exp;
    
    public SysPetExercise() {
    }
    
    public int getId() {
        return this.id;
    }
    
    public int getSoul() {
        return this.soul;
    }
    
    public int getLevel() {
        return this.level;
    }
    
    public int getRelive() {
        return this.relive;
    }
    
    public int getExp() {
        return this.exp;
    }
    
    public void setId(final int id) {
        this.id = id;
    }
    
    public void setSoul(final int soul) {
        this.soul = soul;
    }
    
    public void setLevel(final int level) {
        this.level = level;
    }
    
    public void setRelive(final int relive) {
        this.relive = relive;
    }
    
    public void setExp(final int exp) {
        this.exp = exp;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SysPetExercise)) {
            return false;
        }
        SysPetExercise other = (SysPetExercise)o;
        return other.canEqual(this) && this.getId() == other.getId() && this.getSoul() == other.getSoul() && this.getLevel() == other.getLevel() && this.getRelive() == other.getRelive() && this.getExp() == other.getExp();
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof SysPetExercise;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getId();
        result = result * 59 + this.getSoul();
        result = result * 59 + this.getLevel();
        result = result * 59 + this.getRelive();
        result = result * 59 + this.getExp();
        return result;
    }
    
    @Override
    public String toString() {
        return "SysPetExercise(id=" + this.getId() + ", soul=" + this.getSoul() + ", level=" + this.getLevel() + ", relive=" + this.getRelive() + ", exp=" + this.getExp() + ")";
    }
}
