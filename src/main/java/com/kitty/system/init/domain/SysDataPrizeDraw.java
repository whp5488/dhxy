
package com.kitty.system.init.domain;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Table;
import java.io.Serializable;

@Table("data_prize_draw")
public class SysDataPrizeDraw implements Serializable
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
    private int type;
    
    public SysDataPrizeDraw() {
    }
    
    public SysDataPrizeDraw(final int serial, final int num, final int type) {
        this.serial = serial;
        this.num = num;
        this.type = type;
    }
    
    public SysDataPrizeDraw(final int id, final int serial, final int num, final double rate, final int type) {
        this.id = id;
        this.serial = serial;
        this.num = num;
        this.rate = rate;
        this.type = type;
    }
    
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
    
    public int getType() {
        return this.type;
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
    
    public void setType(final int type) {
        this.type = type;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SysDataPrizeDraw)) {
            return false;
        }
        final SysDataPrizeDraw other = (SysDataPrizeDraw)o;
        return other.canEqual(this) && this.getId() == other.getId() && this.getSerial() == other.getSerial() && this.getNum() == other.getNum() && Double.compare(this.getRate(), other.getRate()) == 0 && this.getType() == other.getType();
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof SysDataPrizeDraw;
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
        return result;
    }
    
    @Override
    public String toString() {
        return "SysDataPrizeDraw(id=" + this.getId() + ", serial=" + this.getSerial() + ", num=" + this.getNum() + ", rate=" + this.getRate() + ", type=" + this.getType() + ")";
    }
}
