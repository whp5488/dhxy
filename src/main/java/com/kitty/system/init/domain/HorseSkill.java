
package com.kitty.system.init.domain;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Table;

@Table("horse_skill")
public class HorseSkill
{
    @Comment("技能编号")
    @Column("serial")
    private int serial;
    @Comment("位置一权重")
    @Column("seat_weight_one")
    private double seatWeightOne;
    @Comment("位置二权重")
    @Column("seat_weight_tow")
    private double seatWeightTow;
    @Comment("位置三权重")
    @Column("seat_weight_three")
    private double seatWeightThree;
    @Comment("品阶")
    @Column("rarity")
    private byte rarity;
    @Comment("类型：0.普通 1.高级")
    @Column("type")
    private byte type;
    @Comment("归属坐骑：1.一座 2.二座 3.三座 4.四座")
    @Column("seat")
    private byte seat;
    @Comment("触发类型")
    @Column("trigger_type")
    private String triggerType;
    @Comment("是否初始化加载，0=否，1=是")
    @Column("is_fight_initialize_load")
    private boolean isFightInitializeLoad;
    
    public int getSerial() {
        return this.serial;
    }
    
    public double getSeatWeightOne() {
        return this.seatWeightOne;
    }
    
    public double getSeatWeightTow() {
        return this.seatWeightTow;
    }
    
    public double getSeatWeightThree() {
        return this.seatWeightThree;
    }
    
    public byte getRarity() {
        return this.rarity;
    }
    
    public byte getType() {
        return this.type;
    }
    
    public byte getSeat() {
        return this.seat;
    }
    
    public String getTriggerType() {
        return this.triggerType;
    }
    
    public boolean isFightInitializeLoad() {
        return this.isFightInitializeLoad;
    }
    
    public void setSerial(final int serial) {
        this.serial = serial;
    }
    
    public void setSeatWeightOne(final double seatWeightOne) {
        this.seatWeightOne = seatWeightOne;
    }
    
    public void setSeatWeightTow(final double seatWeightTow) {
        this.seatWeightTow = seatWeightTow;
    }
    
    public void setSeatWeightThree(final double seatWeightThree) {
        this.seatWeightThree = seatWeightThree;
    }
    
    public void setRarity(final byte rarity) {
        this.rarity = rarity;
    }
    
    public void setType(final byte type) {
        this.type = type;
    }
    
    public void setSeat(final byte seat) {
        this.seat = seat;
    }
    
    public void setTriggerType(final String triggerType) {
        this.triggerType = triggerType;
    }
    
    public void setFightInitializeLoad(final boolean isFightInitializeLoad) {
        this.isFightInitializeLoad = isFightInitializeLoad;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof HorseSkill)) {
            return false;
        }
        final HorseSkill other = (HorseSkill)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getSerial() != other.getSerial()) {
            return false;
        }
        if (Double.compare(this.getSeatWeightOne(), other.getSeatWeightOne()) != 0) {
            return false;
        }
        if (Double.compare(this.getSeatWeightTow(), other.getSeatWeightTow()) != 0) {
            return false;
        }
        if (Double.compare(this.getSeatWeightThree(), other.getSeatWeightThree()) != 0) {
            return false;
        }
        if (this.getRarity() != other.getRarity()) {
            return false;
        }
        if (this.getType() != other.getType()) {
            return false;
        }
        if (this.getSeat() != other.getSeat()) {
            return false;
        }
        final Object this$triggerType = this.getTriggerType();
        final Object other$triggerType = other.getTriggerType();
        if (this$triggerType == null) {
            if (other$triggerType == null) {
                return this.isFightInitializeLoad() == other.isFightInitializeLoad();
            }
        }
        else if (this$triggerType.equals(other$triggerType)) {
            return this.isFightInitializeLoad() == other.isFightInitializeLoad();
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof HorseSkill;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getSerial();
        final long $seatWeightOne = Double.doubleToLongBits(this.getSeatWeightOne());
        result = result * 59 + (int)($seatWeightOne >>> 32 ^ $seatWeightOne);
        final long $seatWeightTow = Double.doubleToLongBits(this.getSeatWeightTow());
        result = result * 59 + (int)($seatWeightTow >>> 32 ^ $seatWeightTow);
        final long $seatWeightThree = Double.doubleToLongBits(this.getSeatWeightThree());
        result = result * 59 + (int)($seatWeightThree >>> 32 ^ $seatWeightThree);
        result = result * 59 + this.getRarity();
        result = result * 59 + this.getType();
        result = result * 59 + this.getSeat();
        final Object $triggerType = this.getTriggerType();
        result = result * 59 + (($triggerType == null) ? 43 : $triggerType.hashCode());
        result = result * 59 + (this.isFightInitializeLoad() ? 79 : 97);
        return result;
    }
    
    @Override
    public String toString() {
        return "HorseSkill(serial=" + this.getSerial() + ", seatWeightOne=" + this.getSeatWeightOne() + ", seatWeightTow=" + this.getSeatWeightTow() + ", seatWeightThree=" + this.getSeatWeightThree() + ", rarity=" + this.getRarity() + ", type=" + this.getType() + ", seat=" + this.getSeat() + ", triggerType=" + this.getTriggerType() + ", isFightInitializeLoad=" + this.isFightInitializeLoad() + ")";
    }
}
