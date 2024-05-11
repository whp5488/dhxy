
package com.kitty.system.init.domain;

import org.nutz.dao.entity.annotation.ColDefine;
import org.nutz.lang.util.NutMap;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Table;

@Table("data_card")
public class SysDataCard
{
    @Column("serial")
    private int serial;
    @Column("name")
    private String name;
    @Column("need_five_grade")
    private int needFiveGrade;
    @Column("need_owner_grade")
    private int needOwnerGrade;
    @Column("expire")
    private int canMerge;
    @Column("rarity")
    private int rarity;
    @Column("base_attr")
    @ColDefine(customType = "text")
    private NutMap baseAttr;
    @Column("five_attr")
    private NutMap fiveAttr;
    @Column("factor")
    private double factor;
    @Column("morph")
    private int morph;
    @Column("reward_rarity")
    private int rewardRarity;
    @Column("expire")
    private int expire;
    
    public int getSerial() {
        return this.serial;
    }
    
    public String getName() {
        return this.name;
    }
    
    public int getNeedFiveGrade() {
        return this.needFiveGrade;
    }
    
    public int getNeedOwnerGrade() {
        return this.needOwnerGrade;
    }
    
    public int getCanMerge() {
        return this.canMerge;
    }
    
    public int getRarity() {
        return this.rarity;
    }
    
    public NutMap getBaseAttr() {
        return this.baseAttr;
    }
    
    public NutMap getFiveAttr() {
        return this.fiveAttr;
    }
    
    public double getFactor() {
        return this.factor;
    }
    
    public int getMorph() {
        return this.morph;
    }
    
    public int getRewardRarity() {
        return this.rewardRarity;
    }
    
    public int getExpire() {
        return this.expire;
    }
    
    public void setSerial(final int serial) {
        this.serial = serial;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public void setNeedFiveGrade(final int needFiveGrade) {
        this.needFiveGrade = needFiveGrade;
    }
    
    public void setNeedOwnerGrade(final int needOwnerGrade) {
        this.needOwnerGrade = needOwnerGrade;
    }
    
    public void setCanMerge(final int canMerge) {
        this.canMerge = canMerge;
    }
    
    public void setRarity(final int rarity) {
        this.rarity = rarity;
    }
    
    public void setBaseAttr(final NutMap baseAttr) {
        this.baseAttr = baseAttr;
    }
    
    public void setFiveAttr(final NutMap fiveAttr) {
        this.fiveAttr = fiveAttr;
    }
    
    public void setFactor(final double factor) {
        this.factor = factor;
    }
    
    public void setMorph(final int morph) {
        this.morph = morph;
    }
    
    public void setRewardRarity(final int rewardRarity) {
        this.rewardRarity = rewardRarity;
    }
    
    public void setExpire(final int expire) {
        this.expire = expire;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SysDataCard)) {
            return false;
        }
        final SysDataCard other = (SysDataCard)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getSerial() != other.getSerial()) {
            return false;
        }
        final Object this$name = this.getName();
        final Object other$name = other.getName();
        Label_0078: {
            if (this$name == null) {
                if (other$name == null) {
                    break Label_0078;
                }
            }
            else if (this$name.equals(other$name)) {
                break Label_0078;
            }
            return false;
        }
        if (this.getNeedFiveGrade() != other.getNeedFiveGrade()) {
            return false;
        }
        if (this.getNeedOwnerGrade() != other.getNeedOwnerGrade()) {
            return false;
        }
        if (this.getCanMerge() != other.getCanMerge()) {
            return false;
        }
        if (this.getRarity() != other.getRarity()) {
            return false;
        }
        final Object this$baseAttr = this.getBaseAttr();
        final Object other$baseAttr = other.getBaseAttr();
        Label_0167: {
            if (this$baseAttr == null) {
                if (other$baseAttr == null) {
                    break Label_0167;
                }
            }
            else if (this$baseAttr.equals(other$baseAttr)) {
                break Label_0167;
            }
            return false;
        }
        final Object this$fiveAttr = this.getFiveAttr();
        final Object other$fiveAttr = other.getFiveAttr();
        if (this$fiveAttr == null) {
            if (other$fiveAttr == null) {
                return Double.compare(this.getFactor(), other.getFactor()) == 0 && this.getMorph() == other.getMorph() && this.getRewardRarity() == other.getRewardRarity() && this.getExpire() == other.getExpire();
            }
        }
        else if (this$fiveAttr.equals(other$fiveAttr)) {
            return Double.compare(this.getFactor(), other.getFactor()) == 0 && this.getMorph() == other.getMorph() && this.getRewardRarity() == other.getRewardRarity() && this.getExpire() == other.getExpire();
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof SysDataCard;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getSerial();
        final Object $name = this.getName();
        result = result * 59 + (($name == null) ? 43 : $name.hashCode());
        result = result * 59 + this.getNeedFiveGrade();
        result = result * 59 + this.getNeedOwnerGrade();
        result = result * 59 + this.getCanMerge();
        result = result * 59 + this.getRarity();
        final Object $baseAttr = this.getBaseAttr();
        result = result * 59 + (($baseAttr == null) ? 43 : $baseAttr.hashCode());
        final Object $fiveAttr = this.getFiveAttr();
        result = result * 59 + (($fiveAttr == null) ? 43 : $fiveAttr.hashCode());
        final long $factor = Double.doubleToLongBits(this.getFactor());
        result = result * 59 + (int)($factor >>> 32 ^ $factor);
        result = result * 59 + this.getMorph();
        result = result * 59 + this.getRewardRarity();
        result = result * 59 + this.getExpire();
        return result;
    }
    
    @Override
    public String toString() {
        return "SysDataCard(serial=" + this.getSerial() + ", name=" + this.getName() + ", needFiveGrade=" + this.getNeedFiveGrade() + ", needOwnerGrade=" + this.getNeedOwnerGrade() + ", canMerge=" + this.getCanMerge() + ", rarity=" + this.getRarity() + ", baseAttr=" + this.getBaseAttr() + ", fiveAttr=" + this.getFiveAttr() + ", factor=" + this.getFactor() + ", morph=" + this.getMorph() + ", rewardRarity=" + this.getRewardRarity() + ", expire=" + this.getExpire() + ")";
    }
}
