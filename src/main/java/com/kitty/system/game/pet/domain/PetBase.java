
package com.kitty.system.game.pet.domain;

import java.beans.ConstructorProperties;
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
        final PetBase other = (PetBase)o;
        if (!other.canEqual(this)) {
            return false;
        }
        final Object this$hp_base = this.getHp_base();
        final Object other$hp_base = other.getHp_base();
        Label_0065: {
            if (this$hp_base == null) {
                if (other$hp_base == null) {
                    break Label_0065;
                }
            }
            else if (this$hp_base.equals(other$hp_base)) {
                break Label_0065;
            }
            return false;
        }
        final Object this$mp_base = this.getMp_base();
        final Object other$mp_base = other.getMp_base();
        Label_0102: {
            if (this$mp_base == null) {
                if (other$mp_base == null) {
                    break Label_0102;
                }
            }
            else if (this$mp_base.equals(other$mp_base)) {
                break Label_0102;
            }
            return false;
        }
        final Object this$ap_base = this.getAp_base();
        final Object other$ap_base = other.getAp_base();
        Label_0139: {
            if (this$ap_base == null) {
                if (other$ap_base == null) {
                    break Label_0139;
                }
            }
            else if (this$ap_base.equals(other$ap_base)) {
                break Label_0139;
            }
            return false;
        }
        final Object this$sp_base = this.getSp_base();
        final Object other$sp_base = other.getSp_base();
        Label_0176: {
            if (this$sp_base == null) {
                if (other$sp_base == null) {
                    break Label_0176;
                }
            }
            else if (this$sp_base.equals(other$sp_base)) {
                break Label_0176;
            }
            return false;
        }
        final Object this$grow_up = this.getGrow_up();
        final Object other$grow_up = other.getGrow_up();
        Label_0213: {
            if (this$grow_up == null) {
                if (other$grow_up == null) {
                    break Label_0213;
                }
            }
            else if (this$grow_up.equals(other$grow_up)) {
                break Label_0213;
            }
            return false;
        }
        final Object this$grade_base = this.getGrade_base();
        final Object other$grade_base = other.getGrade_base();
        Label_0250: {
            if (this$grade_base == null) {
                if (other$grade_base == null) {
                    break Label_0250;
                }
            }
            else if (this$grade_base.equals(other$grade_base)) {
                break Label_0250;
            }
            return false;
        }
        final Object this$score = this.getScore();
        final Object other$score = other.getScore();
        if (this$score == null) {
            if (other$score == null) {
                return true;
            }
        }
        else if (this$score.equals(other$score)) {
            return true;
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof PetBase;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $hp_base = this.getHp_base();
        result = result * 59 + (($hp_base == null) ? 43 : $hp_base.hashCode());
        final Object $mp_base = this.getMp_base();
        result = result * 59 + (($mp_base == null) ? 43 : $mp_base.hashCode());
        final Object $ap_base = this.getAp_base();
        result = result * 59 + (($ap_base == null) ? 43 : $ap_base.hashCode());
        final Object $sp_base = this.getSp_base();
        result = result * 59 + (($sp_base == null) ? 43 : $sp_base.hashCode());
        final Object $grow_up = this.getGrow_up();
        result = result * 59 + (($grow_up == null) ? 43 : $grow_up.hashCode());
        final Object $grade_base = this.getGrade_base();
        result = result * 59 + (($grade_base == null) ? 43 : $grade_base.hashCode());
        final Object $score = this.getScore();
        result = result * 59 + (($score == null) ? 43 : $score.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "PetBase(hp_base=" + this.getHp_base() + ", mp_base=" + this.getMp_base() + ", ap_base=" + this.getAp_base() + ", sp_base=" + this.getSp_base() + ", grow_up=" + this.getGrow_up() + ", grade_base=" + this.getGrade_base() + ", score=" + this.getScore() + ")";
    }
    
    @ConstructorProperties({ "hp_base", "mp_base", "ap_base", "sp_base", "grow_up", "grade_base", "score" })
    public PetBase(final Integer hp_base, final Integer mp_base, final Integer ap_base, final Integer sp_base, final Integer grow_up, final Integer grade_base, final Integer score) {
        this.hp_base = 0;
        this.mp_base = 0;
        this.ap_base = 0;
        this.sp_base = 0;
        this.grow_up = 0;
        this.grade_base = 0;
        this.score = 0;
        this.hp_base = hp_base;
        this.mp_base = mp_base;
        this.ap_base = ap_base;
        this.sp_base = sp_base;
        this.grow_up = grow_up;
        this.grade_base = grade_base;
        this.score = score;
    }
    
    public PetBase() {
        this.hp_base = 0;
        this.mp_base = 0;
        this.ap_base = 0;
        this.sp_base = 0;
        this.grow_up = 0;
        this.grade_base = 0;
        this.score = 0;
    }
}
