package com.kitty.system.init.domain;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Table;
import java.io.Serializable;

@Table("data_charge")
public class SysCharge implements Serializable
{
    @Comment("充值编号")
    @Column("serial")
    private int serial;
    @Comment("金额")
    @Column("amount")
    private int amount;
    @Comment("赠送仙玉")
    @Column("give")
    private long give;
    @Comment("赠送积分")
    @Column("score")
    private long score;
    
    public SysCharge() {
    }
    
    public int getSerial() {
        return this.serial;
    }
    
    public int getAmount() {
        return this.amount;
    }
    
    public long getGive() {
        return this.give;
    }
    
    public long getScore() {
        return this.score;
    }
    
    public void setSerial(final int serial) {
        this.serial = serial;
    }
    
    public void setAmount(final int amount) {
        this.amount = amount;
    }
    
    public void setGive(final long give) {
        this.give = give;
    }
    
    public void setScore(final long score) {
        this.score = score;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SysCharge)) {
            return false;
        }
        SysCharge other = (SysCharge)o;
        return other.canEqual(this) && this.getSerial() == other.getSerial() && this.getAmount() == other.getAmount() && this.getGive() == other.getGive() && this.getScore() == other.getScore();
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof SysCharge;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getSerial();
        result = result * 59 + this.getAmount();
        long $give = this.getGive();
        result = result * 59 + (int)($give >>> 32 ^ $give);
        long $score = this.getScore();
        result = result * 59 + (int)($score >>> 32 ^ $score);
        return result;
    }
    
    @Override
    public String toString() {
        return "SysCharge(serial=" + this.getSerial() + ", amount=" + this.getAmount() + ", give=" + this.getGive() + ", score=" + this.getScore() + ")";
    }
}
