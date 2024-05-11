
package com.kitty.system.init.domain;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Table;
import java.io.Serializable;

@Table("data_magic_box")
public class SysDataMagicBox implements Serializable
{
    @Column("identify")
    private int identify;
    @Column("affiliation")
    private int affiliation;
    @Column("serial")
    private int serial;
    @Column("num")
    private short num;
    @Column("rate")
    private double rate;
    @Column("type")
    private byte type;
    @Column("classify")
    private byte classify;
    @Column("certainly")
    private byte certainly;
    @Column("is_notice")
    private byte isNotice;
    @Column("bd")
    private int bd;
    
    public SysDataMagicBox() {
        this.bd = 0;
    }
    
    public int getIdentify() {
        return this.identify;
    }
    
    public int getAffiliation() {
        return this.affiliation;
    }
    
    public int getSerial() {
        return this.serial;
    }
    
    public short getNum() {
        return this.num;
    }
    
    public double getRate() {
        return this.rate;
    }
    
    public byte getType() {
        return this.type;
    }
    
    public byte getClassify() {
        return this.classify;
    }
    
    public byte getCertainly() {
        return this.certainly;
    }
    
    public byte getIsNotice() {
        return this.isNotice;
    }
    
    public int getBd() {
        return this.bd;
    }
    
    public void setIdentify(final int identify) {
        this.identify = identify;
    }
    
    public void setAffiliation(final int affiliation) {
        this.affiliation = affiliation;
    }
    
    public void setSerial(final int serial) {
        this.serial = serial;
    }
    
    public void setNum(final short num) {
        this.num = num;
    }
    
    public void setRate(final double rate) {
        this.rate = rate;
    }
    
    public void setType(final byte type) {
        this.type = type;
    }
    
    public void setClassify(final byte classify) {
        this.classify = classify;
    }
    
    public void setCertainly(final byte certainly) {
        this.certainly = certainly;
    }
    
    public void setIsNotice(final byte isNotice) {
        this.isNotice = isNotice;
    }
    
    public void setBd(final int bd) {
        this.bd = bd;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SysDataMagicBox)) {
            return false;
        }
        final SysDataMagicBox other = (SysDataMagicBox)o;
        return other.canEqual(this) && this.getIdentify() == other.getIdentify() && this.getAffiliation() == other.getAffiliation() && this.getSerial() == other.getSerial() && this.getNum() == other.getNum() && Double.compare(this.getRate(), other.getRate()) == 0 && this.getType() == other.getType() && this.getClassify() == other.getClassify() && this.getCertainly() == other.getCertainly() && this.getIsNotice() == other.getIsNotice() && this.getBd() == other.getBd();
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof SysDataMagicBox;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getIdentify();
        result = result * 59 + this.getAffiliation();
        result = result * 59 + this.getSerial();
        result = result * 59 + this.getNum();
        final long $rate = Double.doubleToLongBits(this.getRate());
        result = result * 59 + (int)($rate >>> 32 ^ $rate);
        result = result * 59 + this.getType();
        result = result * 59 + this.getClassify();
        result = result * 59 + this.getCertainly();
        result = result * 59 + this.getIsNotice();
        result = result * 59 + this.getBd();
        return result;
    }
    
    @Override
    public String toString() {
        return "SysDataMagicBox(identify=" + this.getIdentify() + ", affiliation=" + this.getAffiliation() + ", serial=" + this.getSerial() + ", num=" + this.getNum() + ", rate=" + this.getRate() + ", type=" + this.getType() + ", classify=" + this.getClassify() + ", certainly=" + this.getCertainly() + ", isNotice=" + this.getIsNotice() + ", bd=" + this.getBd() + ")";
    }
}
