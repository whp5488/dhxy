
package com.kitty.system.init.domain;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Table;
import java.io.Serializable;

@Table("data_lottery_rare")
public class SysDataLotteryRare implements Serializable
{
    private static final long serialVersionUID = 1L;
    @Column("id")
    private int id;
    @Column("serial")
    private int serial;
    @Column("num")
    private int num;
    @Column("rate")
    private double rate;
    @Column("type")
    private byte type;
    @Column("is_notice")
    private byte isNotice;
    
    public int getId() {
        return this.id;
    }
    
    public int getSerial() {
        return this.serial;
    }
    
    public int getNum() {
        return this.num;
    }
    
    public double getRate() {
        return this.rate;
    }
    
    public byte getType() {
        return this.type;
    }
    
    public byte getIsNotice() {
        return this.isNotice;
    }
    
    public void setId(final int id) {
        this.id = id;
    }
    
    public void setSerial(final int serial) {
        this.serial = serial;
    }
    
    public void setNum(final int num) {
        this.num = num;
    }
    
    public void setRate(final double rate) {
        this.rate = rate;
    }
    
    public void setType(final byte type) {
        this.type = type;
    }
    
    public void setIsNotice(final byte isNotice) {
        this.isNotice = isNotice;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SysDataLotteryRare)) {
            return false;
        }
        final SysDataLotteryRare other = (SysDataLotteryRare)o;
        return other.canEqual(this) && this.getId() == other.getId() && this.getSerial() == other.getSerial() && this.getNum() == other.getNum() && Double.compare(this.getRate(), other.getRate()) == 0 && this.getType() == other.getType() && this.getIsNotice() == other.getIsNotice();
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof SysDataLotteryRare;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getId();
        result = result * 59 + this.getSerial();
        result = result * 59 + this.getNum();
        final long $rate = Double.doubleToLongBits(this.getRate());
        result = result * 59 + (int)($rate >>> 32 ^ $rate);
        result = result * 59 + this.getType();
        result = result * 59 + this.getIsNotice();
        return result;
    }
    
    @Override
    public String toString() {
        return "SysDataLotteryRare(id=" + this.getId() + ", serial=" + this.getSerial() + ", num=" + this.getNum() + ", rate=" + this.getRate() + ", type=" + this.getType() + ", isNotice=" + this.getIsNotice() + ")";
    }
}
