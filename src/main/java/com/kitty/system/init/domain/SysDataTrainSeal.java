package com.kitty.system.init.domain;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Table;
import java.io.Serializable;

@Table("data_train_seal")
public class SysDataTrainSeal implements Serializable
{
    @Comment("信物编号")
    @Column("serial")
    private int serial;
    @Comment("信物名称")
    @Column("name")
    private String name;
    @Comment("归属")
    @Column("belong")
    private int belong;
    @Comment("洗练类型")
    @Column("refine_type")
    private int refineType;
    @Comment("天赋解锁等级")
    @Column("unlock_lv")
    private int unlockLv;
    @Comment("兑位")
    @Column("dui")
    private int dui;
    @Comment("艮位")
    @Column("gen")
    private int gen;
    @Comment("坎位")
    @Column("kan")
    private int kan;
    @Comment("坤位")
    @Column("kun")
    private int kun;
    @Comment("离位")
    @Column("li")
    private int li;
    @Comment("巽位")
    @Column("xun")
    private int xun;
    @Comment("乾位")
    @Column("qian")
    private int qian;
    @Comment("震位")
    @Column("zhen")
    private int zhen;
    
    public SysDataTrainSeal() {
    }
    
    public int getSerial() {
        return this.serial;
    }
    
    public String getName() {
        return this.name;
    }
    
    public int getBelong() {
        return this.belong;
    }
    
    public int getRefineType() {
        return this.refineType;
    }
    
    public int getUnlockLv() {
        return this.unlockLv;
    }
    
    public int getDui() {
        return this.dui;
    }
    
    public int getGen() {
        return this.gen;
    }
    
    public int getKan() {
        return this.kan;
    }
    
    public int getKun() {
        return this.kun;
    }
    
    public int getLi() {
        return this.li;
    }
    
    public int getXun() {
        return this.xun;
    }
    
    public int getQian() {
        return this.qian;
    }
    
    public int getZhen() {
        return this.zhen;
    }
    
    public void setSerial(final int serial) {
        this.serial = serial;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public void setBelong(final int belong) {
        this.belong = belong;
    }
    
    public void setRefineType(final int refineType) {
        this.refineType = refineType;
    }
    
    public void setUnlockLv(final int unlockLv) {
        this.unlockLv = unlockLv;
    }
    
    public void setDui(final int dui) {
        this.dui = dui;
    }
    
    public void setGen(final int gen) {
        this.gen = gen;
    }
    
    public void setKan(final int kan) {
        this.kan = kan;
    }
    
    public void setKun(final int kun) {
        this.kun = kun;
    }
    
    public void setLi(final int li) {
        this.li = li;
    }
    
    public void setXun(final int xun) {
        this.xun = xun;
    }
    
    public void setQian(final int qian) {
        this.qian = qian;
    }
    
    public void setZhen(final int zhen) {
        this.zhen = zhen;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SysDataTrainSeal)) {
            return false;
        }
        SysDataTrainSeal other = (SysDataTrainSeal)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getSerial() != other.getSerial()) {
            return false;
        }
        Object this$name = this.getName();
        Object other$name = other.getName();
        return ((this$name != null) ? this$name.equals(other$name) : (other$name == null)) && this.getBelong() == other.getBelong() && this.getRefineType() == other.getRefineType() && this.getUnlockLv() == other.getUnlockLv() && this.getDui() == other.getDui() && this.getGen() == other.getGen() && this.getKan() == other.getKan() && this.getKun() == other.getKun() && this.getLi() == other.getLi() && this.getXun() == other.getXun() && this.getQian() == other.getQian() && this.getZhen() == other.getZhen();
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof SysDataTrainSeal;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getSerial();
        Object $name = this.getName();
        result = result * 59 + (($name == null) ? 43 : $name.hashCode());
        result = result * 59 + this.getBelong();
        result = result * 59 + this.getRefineType();
        result = result * 59 + this.getUnlockLv();
        result = result * 59 + this.getDui();
        result = result * 59 + this.getGen();
        result = result * 59 + this.getKan();
        result = result * 59 + this.getKun();
        result = result * 59 + this.getLi();
        result = result * 59 + this.getXun();
        result = result * 59 + this.getQian();
        result = result * 59 + this.getZhen();
        return result;
    }
    
    @Override
    public String toString() {
        return "SysDataTrainSeal(serial=" + this.getSerial() + ", name=" + this.getName() + ", belong=" + this.getBelong() + ", refineType=" + this.getRefineType() + ", unlockLv=" + this.getUnlockLv() + ", dui=" + this.getDui() + ", gen=" + this.getGen() + ", kan=" + this.getKan() + ", kun=" + this.getKun() + ", li=" + this.getLi() + ", xun=" + this.getXun() + ", qian=" + this.getQian() + ", zhen=" + this.getZhen() + ")";
    }
}
