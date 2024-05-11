
package com.kitty.system.init.domain;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Table;

@Table("data_card_five_exercise")
public class SysDataCardFiveExercise
{
    @Comment("修炼等级")
    @Column("grade")
    private int grade;
    @Comment("金经验")
    @Column("gold")
    private int gold;
    @Comment("木经验")
    @Column("wood")
    private int wood;
    @Comment("水经验")
    @Column("water")
    private int water;
    @Comment("火经验")
    @Column("fire")
    private int fire;
    @Comment("土经验")
    @Column("soil")
    private int soil;
    @Comment("五行升级需要的贡币")
    @Column("wx")
    private Integer wx;
    
    public SysDataCardFiveExercise() {
        this.grade = 0;
        this.gold = 0;
        this.wood = 0;
        this.water = 0;
        this.fire = 0;
        this.soil = 0;
        this.wx = 0;
    }
    
    public int getGrade() {
        return this.grade;
    }
    
    public int getGold() {
        return this.gold;
    }
    
    public int getWood() {
        return this.wood;
    }
    
    public int getWater() {
        return this.water;
    }
    
    public int getFire() {
        return this.fire;
    }
    
    public int getSoil() {
        return this.soil;
    }
    
    public Integer getWx() {
        return this.wx;
    }
    
    public void setGrade(final int grade) {
        this.grade = grade;
    }
    
    public void setGold(final int gold) {
        this.gold = gold;
    }
    
    public void setWood(final int wood) {
        this.wood = wood;
    }
    
    public void setWater(final int water) {
        this.water = water;
    }
    
    public void setFire(final int fire) {
        this.fire = fire;
    }
    
    public void setSoil(final int soil) {
        this.soil = soil;
    }
    
    public void setWx(final Integer wx) {
        this.wx = wx;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SysDataCardFiveExercise)) {
            return false;
        }
        final SysDataCardFiveExercise other = (SysDataCardFiveExercise)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getGrade() != other.getGrade()) {
            return false;
        }
        if (this.getGold() != other.getGold()) {
            return false;
        }
        if (this.getWood() != other.getWood()) {
            return false;
        }
        if (this.getWater() != other.getWater()) {
            return false;
        }
        if (this.getFire() != other.getFire()) {
            return false;
        }
        if (this.getSoil() != other.getSoil()) {
            return false;
        }
        final Object this$wx = this.getWx();
        final Object other$wx = other.getWx();
        if (this$wx == null) {
            if (other$wx == null) {
                return true;
            }
        }
        else if (this$wx.equals(other$wx)) {
            return true;
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof SysDataCardFiveExercise;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getGrade();
        result = result * 59 + this.getGold();
        result = result * 59 + this.getWood();
        result = result * 59 + this.getWater();
        result = result * 59 + this.getFire();
        result = result * 59 + this.getSoil();
        final Object $wx = this.getWx();
        result = result * 59 + (($wx == null) ? 43 : $wx.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "SysDataCardFiveExercise(grade=" + this.getGrade() + ", gold=" + this.getGold() + ", wood=" + this.getWood() + ", water=" + this.getWater() + ", fire=" + this.getFire() + ", soil=" + this.getSoil() + ", wx=" + this.getWx() + ")";
    }
}
