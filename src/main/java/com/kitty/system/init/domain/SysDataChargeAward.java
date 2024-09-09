package com.kitty.system.init.domain;

import org.nutz.dao.entity.annotation.Default;
import com.kitty.system.init.domain.vo.Param;
import java.util.List;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Table;
import java.io.Serializable;

@Table("data_charge_award")
public class SysDataChargeAward implements Serializable
{
    private static final long serialVersionUID = 1L;
    @Comment("奖励编号")
    @Column("serial")
    private int serial;
    @Comment("累计金额")
    @Column("amount")
    private long amount;
    @Comment("奖励")
    @Column("award")
    @Default("[]")
    private List<Param> award;
    @Comment("类型：0.充值奖励 1.每日充值奖励")
    @Column("type")
    private byte type;
    
    public SysDataChargeAward() {
    }
    
    public int getSerial() {
        return this.serial;
    }
    
    public long getAmount() {
        return this.amount;
    }
    
    public List<Param> getAward() {
        return this.award;
    }
    
    public byte getType() {
        return this.type;
    }
    
    public void setSerial(final int serial) {
        this.serial = serial;
    }
    
    public void setAmount(final long amount) {
        this.amount = amount;
    }
    
    public void setAward(final List<Param> award) {
        this.award = award;
    }
    
    public void setType(final byte type) {
        this.type = type;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SysDataChargeAward)) {
            return false;
        }
        SysDataChargeAward other = (SysDataChargeAward)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getSerial() != other.getSerial()) {
            return false;
        }
        if (this.getAmount() != other.getAmount()) {
            return false;
        }
        Object this$award = this.getAward();
        Object other$award = other.getAward();
        return ((this$award != null) ? this$award.equals(other$award) : (other$award == null)) && this.getType() == other.getType();
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof SysDataChargeAward;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getSerial();
        long $amount = this.getAmount();
        result = result * 59 + (int)($amount >>> 32 ^ $amount);
        Object $award = this.getAward();
        result = result * 59 + (($award == null) ? 43 : $award.hashCode());
        result = result * 59 + this.getType();
        return result;
    }
    
    @Override
    public String toString() {
        return "SysDataChargeAward(serial=" + this.getSerial() + ", amount=" + this.getAmount() + ", award=" + this.getAward() + ", type=" + (int)this.getType() + ")";
    }
}
