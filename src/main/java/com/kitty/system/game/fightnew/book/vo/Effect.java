
package com.kitty.system.game.fightnew.book.vo;

import java.util.ArrayList;
import com.kitty.system.common.enter.FiedValue;
import java.util.List;

public class Effect
{
    private int type;
    private int time;
    private int hurt;
    private double hit;
    private boolean isTargetHit;
    private List<FiedValue> list;
    private String otherType;
    private Integer SkillId;
    
    public Effect() {
        this.type = 0;
        this.time = 0;
        this.hurt = 0;
        this.hit = -1.0;
        this.isTargetHit = true;
    }
    
    public Effect(final int type, final int hurt) {
        this.type = 0;
        this.time = 0;
        this.hurt = 0;
        this.hit = -1.0;
        this.isTargetHit = true;
        this.type = type;
        this.hurt = hurt;
    }
    
    public Effect(final int type, final int time, final int hurt, final double hit, final List<FiedValue> list) {
        this.type = 0;
        this.time = 0;
        this.hurt = 0;
        this.hit = -1.0;
        this.isTargetHit = true;
        this.type = type;
        this.time = time;
        this.hurt = hurt;
        this.hit = hit;
        this.list = list;
    }
    
    public List<FiedValue> getOrCreateList() {
        if (this.list == null) {
            this.list = new ArrayList<FiedValue>();
        }
        return this.list;
    }
    
    public int getType() {
        return this.type;
    }
    
    public int getTime() {
        return this.time;
    }
    
    public int getHurt() {
        return this.hurt;
    }
    
    public double getHit() {
        return this.hit;
    }
    
    public boolean isTargetHit() {
        return this.isTargetHit;
    }
    
    public List<FiedValue> getList() {
        return this.list;
    }
    
    public String getOtherType() {
        return this.otherType;
    }
    
    public Integer getSkillId() {
        return this.SkillId;
    }
    
    public void setType(final int type) {
        this.type = type;
    }
    
    public void setTime(final int time) {
        this.time = time;
    }
    
    public void setHurt(final int hurt) {
        this.hurt = hurt;
    }
    
    public void setHit(final double hit) {
        this.hit = hit;
    }
    
    public void setTargetHit(final boolean isTargetHit) {
        this.isTargetHit = isTargetHit;
    }
    
    public void setList(final List<FiedValue> list) {
        this.list = list;
    }
    
    public void setOtherType(final String otherType) {
        this.otherType = otherType;
    }
    
    public void setSkillId(final Integer SkillId) {
        this.SkillId = SkillId;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Effect)) {
            return false;
        }
        final Effect other = (Effect)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getType() != other.getType()) {
            return false;
        }
        if (this.getTime() != other.getTime()) {
            return false;
        }
        if (this.getHurt() != other.getHurt()) {
            return false;
        }
        if (Double.compare(this.getHit(), other.getHit()) != 0) {
            return false;
        }
        if (this.isTargetHit() != other.isTargetHit()) {
            return false;
        }
        final Object this$list = this.getList();
        final Object other$list = other.getList();
        Label_0133: {
            if (this$list == null) {
                if (other$list == null) {
                    break Label_0133;
                }
            }
            else if (this$list.equals(other$list)) {
                break Label_0133;
            }
            return false;
        }
        final Object this$otherType = this.getOtherType();
        final Object other$otherType = other.getOtherType();
        Label_0170: {
            if (this$otherType == null) {
                if (other$otherType == null) {
                    break Label_0170;
                }
            }
            else if (this$otherType.equals(other$otherType)) {
                break Label_0170;
            }
            return false;
        }
        final Object this$SkillId = this.getSkillId();
        final Object other$SkillId = other.getSkillId();
        if (this$SkillId == null) {
            if (other$SkillId == null) {
                return true;
            }
        }
        else if (this$SkillId.equals(other$SkillId)) {
            return true;
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof Effect;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getType();
        result = result * 59 + this.getTime();
        result = result * 59 + this.getHurt();
        final long $hit = Double.doubleToLongBits(this.getHit());
        result = result * 59 + (int)($hit >>> 32 ^ $hit);
        result = result * 59 + (this.isTargetHit() ? 79 : 97);
        final Object $list = this.getList();
        result = result * 59 + (($list == null) ? 43 : $list.hashCode());
        final Object $otherType = this.getOtherType();
        result = result * 59 + (($otherType == null) ? 43 : $otherType.hashCode());
        final Object $SkillId = this.getSkillId();
        result = result * 59 + (($SkillId == null) ? 43 : $SkillId.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "Effect(type=" + this.getType() + ", time=" + this.getTime() + ", hurt=" + this.getHurt() + ", hit=" + this.getHit() + ", isTargetHit=" + this.isTargetHit() + ", list=" + this.getList() + ", otherType=" + this.getOtherType() + ", SkillId=" + this.getSkillId() + ")";
    }
}
