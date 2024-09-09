package com.kitty.system.init.domain;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Table;
import java.io.Serializable;

@Table("data_train_exp")
public class SysDataTrainExp implements Serializable
{
    @Comment("等级")
    @Column("grade")
    private int grade;
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
    
    public SysDataTrainExp() {
    }
    
    public int getGrade() {
        return this.grade;
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
    
    public void setGrade(final int grade) {
        this.grade = grade;
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
        if (!(o instanceof SysDataTrainExp)) {
            return false;
        }
        SysDataTrainExp other = (SysDataTrainExp)o;
        return other.canEqual(this) && this.getGrade() == other.getGrade() && this.getDui() == other.getDui() && this.getGen() == other.getGen() && this.getKan() == other.getKan() && this.getKun() == other.getKun() && this.getLi() == other.getLi() && this.getXun() == other.getXun() && this.getQian() == other.getQian() && this.getZhen() == other.getZhen();
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof SysDataTrainExp;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getGrade();
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
        return "SysDataTrainExp(grade=" + this.getGrade() + ", dui=" + this.getDui() + ", gen=" + this.getGen() + ", kan=" + this.getKan() + ", kun=" + this.getKun() + ", li=" + this.getLi() + ", xun=" + this.getXun() + ", qian=" + this.getQian() + ", zhen=" + this.getZhen() + ")";
    }
}
