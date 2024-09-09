package com.kitty.system.game.pet.domain;

import java.io.Serializable;

public class PetBase implements Serializable
{
    private Integer hp_base;
    private Integer mp_base;
    private Integer ap_base;
    private Integer sp_base;
    private Integer grow_up;
    private Integer grade_base;
    private Integer score;
    
    public Integer getHp_base() {
        return this.hp_base;
    }
    
    public Integer getMp_base() {
        return this.mp_base;
    }
    
    public Integer getAp_base() {
        return this.ap_base;
    }
    
    public Integer getSp_base() {
        return this.sp_base;
    }
    
    public Integer getGrow_up() {
        return this.grow_up;
    }
    
    public Integer getGrade_base() {
        return this.grade_base;
    }
    
    public Integer getScore() {
        return this.score;
    }
    
    public void setHp_base(final Integer hp_base) {
        this.hp_base = hp_base;
    }
    
    public void setMp_base(final Integer mp_base) {
        this.mp_base = mp_base;
    }
    
    public void setAp_base(final Integer ap_base) {
        this.ap_base = ap_base;
    }
    
    public void setSp_base(final Integer sp_base) {
        this.sp_base = sp_base;
    }
    
    public void setGrow_up(final Integer grow_up) {
        this.grow_up = grow_up;
    }
    
    public void setGrade_base(final Integer grade_base) {
        this.grade_base = grade_base;
    }
    
    public void setScore(final Integer score) {
        this.score = score;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof PetBase)) {
            return false;
        }
        PetBase other = (PetBase)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$hp_base = this.getHp_base();
        Object other$hp_base = other.getHp_base();
        if (!((this$hp_base != null) ? this$hp_base.equals(other$hp_base) : (other$hp_base == null))) {
            return false;
        }
        Object this$mp_base = this.getMp_base();
        Object other$mp_base = other.getMp_base();
        if (!((this$mp_base != null) ? this$mp_base.equals(other$mp_base) : (other$mp_base == null))) {
            return false;
        }
        Object this$ap_base = this.getAp_base();
        Object other$ap_base = other.getAp_base();
        if (!((this$ap_base != null) ? this$ap_base.equals(other$ap_base) : (other$ap_base == null))) {
            return false;
        }
        Object this$sp_base = this.getSp_base();
        Object other$sp_base = other.getSp_base();
        if (!((this$sp_base != null) ? this$sp_base.equals(other$sp_base) : (other$sp_base == null))) {
            return false;
        }
        Object this$grow_up = this.getGrow_up();
        Object other$grow_up = other.getGrow_up();
        if (!((this$grow_up != null) ? this$grow_up.equals(other$grow_up) : (other$grow_up == null))) {
            return false;
        }
        Object this$grade_base = this.getGrade_base();
        Object other$grade_base = other.getGrade_base();
        if (!((this$grade_base != null) ? this$grade_base.equals(other$grade_base) : (other$grade_base == null))) {
            return false;
        }
        Object this$score = this.getScore();
        Object other$score = other.getScore();
        return (this$score != null) ? this$score.equals(other$score) : (other$score == null);
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof PetBase;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Object $hp_base = this.getHp_base();
        result = result * 59 + (($hp_base == null) ? 43 : $hp_base.hashCode());
        Object $mp_base = this.getMp_base();
        result = result * 59 + (($mp_base == null) ? 43 : $mp_base.hashCode());
        Object $ap_base = this.getAp_base();
        result = result * 59 + (($ap_base == null) ? 43 : $ap_base.hashCode());
        Object $sp_base = this.getSp_base();
        result = result * 59 + (($sp_base == null) ? 43 : $sp_base.hashCode());
        Object $grow_up = this.getGrow_up();
        result = result * 59 + (($grow_up == null) ? 43 : $grow_up.hashCode());
        Object $grade_base = this.getGrade_base();
        result = result * 59 + (($grade_base == null) ? 43 : $grade_base.hashCode());
        Object $score = this.getScore();
        result = result * 59 + (($score == null) ? 43 : $score.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "PetBase(hp_base=" + this.getHp_base() + ", mp_base=" + this.getMp_base() + ", ap_base=" + this.getAp_base() + ", sp_base=" + this.getSp_base() + ", grow_up=" + this.getGrow_up() + ", grade_base=" + this.getGrade_base() + ", score=" + this.getScore() + ")";
    }
    
    public PetBase(final Integer hp_base, final Integer mp_base, final Integer ap_base, final Integer sp_base, final Integer grow_up, final Integer grade_base, final Integer score) {
        this.hp_base = Integer.valueOf(0);
        this.mp_base = Integer.valueOf(0);
        this.ap_base = Integer.valueOf(0);
        this.sp_base = Integer.valueOf(0);
        this.grow_up = Integer.valueOf(0);
        this.grade_base = Integer.valueOf(0);
        this.score = Integer.valueOf(0);
        this.hp_base = hp_base;
        this.mp_base = mp_base;
        this.ap_base = ap_base;
        this.sp_base = sp_base;
        this.grow_up = grow_up;
        this.grade_base = grade_base;
        this.score = score;
    }
    
    public PetBase() {
        this.hp_base = Integer.valueOf(0);
        this.mp_base = Integer.valueOf(0);
        this.ap_base = Integer.valueOf(0);
        this.sp_base = Integer.valueOf(0);
        this.grow_up = Integer.valueOf(0);
        this.grade_base = Integer.valueOf(0);
        this.score = Integer.valueOf(0);
    }
}
