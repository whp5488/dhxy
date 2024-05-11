
package com.kitty.system.init.domain;

import org.nutz.dao.entity.annotation.Default;
import java.util.List;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Table;

@Table("data_pet_acsry")
public class SysPetAcsry
{
    @Column("serial")
    private int serial;
    @Column("name")
    private String name;
    @Column("adv_attr_lib")
    @Default("[]")
    private List<Integer> baseAttr;
    @Column("attr_lib")
    @Default("[]")
    private List<String> attrLib;
    @Column("base_score")
    private int baseScore;
    @Column("grade")
    private byte grade;
    @Column("pos")
    private int pos;
    @Column("max_endure")
    private int maxEndure;
    @Column("need_grade")
    private int needGrade;
    @Column("need_rei")
    private int needRei;
    @Column("type")
    private int type;
    
    public SysPetAcsry() {
        this.baseScore = 0;
        this.grade = 0;
        this.maxEndure = 1000;
        this.needGrade = 0;
        this.needRei = 0;
    }
    
    public int getSerial() {
        return this.serial;
    }
    
    public String getName() {
        return this.name;
    }
    
    public List<Integer> getBaseAttr() {
        return this.baseAttr;
    }
    
    public List<String> getAttrLib() {
        return this.attrLib;
    }
    
    public int getBaseScore() {
        return this.baseScore;
    }
    
    public byte getGrade() {
        return this.grade;
    }
    
    public int getPos() {
        return this.pos;
    }
    
    public int getMaxEndure() {
        return this.maxEndure;
    }
    
    public int getNeedGrade() {
        return this.needGrade;
    }
    
    public int getNeedRei() {
        return this.needRei;
    }
    
    public int getType() {
        return this.type;
    }
    
    public void setSerial(final int serial) {
        this.serial = serial;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public void setBaseAttr(final List<Integer> baseAttr) {
        this.baseAttr = baseAttr;
    }
    
    public void setAttrLib(final List<String> attrLib) {
        this.attrLib = attrLib;
    }
    
    public void setBaseScore(final int baseScore) {
        this.baseScore = baseScore;
    }
    
    public void setGrade(final byte grade) {
        this.grade = grade;
    }
    
    public void setPos(final int pos) {
        this.pos = pos;
    }
    
    public void setMaxEndure(final int maxEndure) {
        this.maxEndure = maxEndure;
    }
    
    public void setNeedGrade(final int needGrade) {
        this.needGrade = needGrade;
    }
    
    public void setNeedRei(final int needRei) {
        this.needRei = needRei;
    }
    
    public void setType(final int type) {
        this.type = type;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SysPetAcsry)) {
            return false;
        }
        final SysPetAcsry other = (SysPetAcsry)o;
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
        final Object this$baseAttr = this.getBaseAttr();
        final Object other$baseAttr = other.getBaseAttr();
        Label_0115: {
            if (this$baseAttr == null) {
                if (other$baseAttr == null) {
                    break Label_0115;
                }
            }
            else if (this$baseAttr.equals(other$baseAttr)) {
                break Label_0115;
            }
            return false;
        }
        final Object this$attrLib = this.getAttrLib();
        final Object other$attrLib = other.getAttrLib();
        if (this$attrLib == null) {
            if (other$attrLib == null) {
                return this.getBaseScore() == other.getBaseScore() && this.getGrade() == other.getGrade() && this.getPos() == other.getPos() && this.getMaxEndure() == other.getMaxEndure() && this.getNeedGrade() == other.getNeedGrade() && this.getNeedRei() == other.getNeedRei() && this.getType() == other.getType();
            }
        }
        else if (this$attrLib.equals(other$attrLib)) {
            return this.getBaseScore() == other.getBaseScore() && this.getGrade() == other.getGrade() && this.getPos() == other.getPos() && this.getMaxEndure() == other.getMaxEndure() && this.getNeedGrade() == other.getNeedGrade() && this.getNeedRei() == other.getNeedRei() && this.getType() == other.getType();
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof SysPetAcsry;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getSerial();
        final Object $name = this.getName();
        result = result * 59 + (($name == null) ? 43 : $name.hashCode());
        final Object $baseAttr = this.getBaseAttr();
        result = result * 59 + (($baseAttr == null) ? 43 : $baseAttr.hashCode());
        final Object $attrLib = this.getAttrLib();
        result = result * 59 + (($attrLib == null) ? 43 : $attrLib.hashCode());
        result = result * 59 + this.getBaseScore();
        result = result * 59 + this.getGrade();
        result = result * 59 + this.getPos();
        result = result * 59 + this.getMaxEndure();
        result = result * 59 + this.getNeedGrade();
        result = result * 59 + this.getNeedRei();
        result = result * 59 + this.getType();
        return result;
    }
    
    @Override
    public String toString() {
        return "SysPetAcsry(serial=" + this.getSerial() + ", name=" + this.getName() + ", baseAttr=" + this.getBaseAttr() + ", attrLib=" + this.getAttrLib() + ", baseScore=" + this.getBaseScore() + ", grade=" + this.getGrade() + ", pos=" + this.getPos() + ", maxEndure=" + this.getMaxEndure() + ", needGrade=" + this.getNeedGrade() + ", needRei=" + this.getNeedRei() + ", type=" + this.getType() + ")";
    }
}
