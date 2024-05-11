
package com.kitty.system.init.domain;

import org.nutz.dao.entity.annotation.Default;
import java.util.List;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Table;

@Table("data_acsry")
public class SysDataAcsry
{
    @Column("serial")
    private int serial;
    @Column("name")
    private String name;
    @Column("base_attr")
    private String baseAttr;
    @Column("base_score")
    private int baseScore;
    @Column("pos")
    private int pos;
    @Column("max_endure")
    private int maxEndure;
    @Column("need_gg")
    private int needGg;
    @Column("need_lx")
    private int needLx;
    @Column("need_ll")
    private int needLl;
    @Column("need_mj")
    private int needMj;
    @Column("need_grade")
    private int needGrade;
    @Column("need_rei")
    private int needRei;
    @Column("owner_role")
    @Default("[]")
    private List<Integer> ownerRole;
    @Column("sex")
    private int sex;
    @Column("rarity")
    private int rarity;
    @Column("shape")
    private int shape;
    @Column("rnd_range")
    private double rndRange;
    @Column("basic_price")
    private long basicPrice;
    @Column("suite_type")
    private String suiteType;
    
    public SysDataAcsry() {
        this.baseScore = 0;
        this.maxEndure = 1000;
        this.needGg = 0;
        this.needLx = 0;
        this.needLl = 0;
        this.needMj = 0;
        this.needGrade = 0;
        this.needRei = 0;
        this.rarity = 1;
    }
    
    public int getSerial() {
        return this.serial;
    }
    
    public String getName() {
        return this.name;
    }
    
    public String getBaseAttr() {
        return this.baseAttr;
    }
    
    public int getBaseScore() {
        return this.baseScore;
    }
    
    public int getPos() {
        return this.pos;
    }
    
    public int getMaxEndure() {
        return this.maxEndure;
    }
    
    public int getNeedGg() {
        return this.needGg;
    }
    
    public int getNeedLx() {
        return this.needLx;
    }
    
    public int getNeedLl() {
        return this.needLl;
    }
    
    public int getNeedMj() {
        return this.needMj;
    }
    
    public int getNeedGrade() {
        return this.needGrade;
    }
    
    public int getNeedRei() {
        return this.needRei;
    }
    
    public List<Integer> getOwnerRole() {
        return this.ownerRole;
    }
    
    public int getSex() {
        return this.sex;
    }
    
    public int getRarity() {
        return this.rarity;
    }
    
    public int getShape() {
        return this.shape;
    }
    
    public double getRndRange() {
        return this.rndRange;
    }
    
    public long getBasicPrice() {
        return this.basicPrice;
    }
    
    public String getSuiteType() {
        return this.suiteType;
    }
    
    public void setSerial(final int serial) {
        this.serial = serial;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public void setBaseAttr(final String baseAttr) {
        this.baseAttr = baseAttr;
    }
    
    public void setBaseScore(final int baseScore) {
        this.baseScore = baseScore;
    }
    
    public void setPos(final int pos) {
        this.pos = pos;
    }
    
    public void setMaxEndure(final int maxEndure) {
        this.maxEndure = maxEndure;
    }
    
    public void setNeedGg(final int needGg) {
        this.needGg = needGg;
    }
    
    public void setNeedLx(final int needLx) {
        this.needLx = needLx;
    }
    
    public void setNeedLl(final int needLl) {
        this.needLl = needLl;
    }
    
    public void setNeedMj(final int needMj) {
        this.needMj = needMj;
    }
    
    public void setNeedGrade(final int needGrade) {
        this.needGrade = needGrade;
    }
    
    public void setNeedRei(final int needRei) {
        this.needRei = needRei;
    }
    
    public void setOwnerRole(final List<Integer> ownerRole) {
        this.ownerRole = ownerRole;
    }
    
    public void setSex(final int sex) {
        this.sex = sex;
    }
    
    public void setRarity(final int rarity) {
        this.rarity = rarity;
    }
    
    public void setShape(final int shape) {
        this.shape = shape;
    }
    
    public void setRndRange(final double rndRange) {
        this.rndRange = rndRange;
    }
    
    public void setBasicPrice(final long basicPrice) {
        this.basicPrice = basicPrice;
    }
    
    public void setSuiteType(final String suiteType) {
        this.suiteType = suiteType;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SysDataAcsry)) {
            return false;
        }
        final SysDataAcsry other = (SysDataAcsry)o;
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
        if (this.getBaseScore() != other.getBaseScore()) {
            return false;
        }
        if (this.getPos() != other.getPos()) {
            return false;
        }
        if (this.getMaxEndure() != other.getMaxEndure()) {
            return false;
        }
        if (this.getNeedGg() != other.getNeedGg()) {
            return false;
        }
        if (this.getNeedLx() != other.getNeedLx()) {
            return false;
        }
        if (this.getNeedLl() != other.getNeedLl()) {
            return false;
        }
        if (this.getNeedMj() != other.getNeedMj()) {
            return false;
        }
        if (this.getNeedGrade() != other.getNeedGrade()) {
            return false;
        }
        if (this.getNeedRei() != other.getNeedRei()) {
            return false;
        }
        final Object this$ownerRole = this.getOwnerRole();
        final Object other$ownerRole = other.getOwnerRole();
        Label_0269: {
            if (this$ownerRole == null) {
                if (other$ownerRole == null) {
                    break Label_0269;
                }
            }
            else if (this$ownerRole.equals(other$ownerRole)) {
                break Label_0269;
            }
            return false;
        }
        if (this.getSex() != other.getSex()) {
            return false;
        }
        if (this.getRarity() != other.getRarity()) {
            return false;
        }
        if (this.getShape() != other.getShape()) {
            return false;
        }
        if (Double.compare(this.getRndRange(), other.getRndRange()) != 0) {
            return false;
        }
        if (this.getBasicPrice() != other.getBasicPrice()) {
            return false;
        }
        final Object this$suiteType = this.getSuiteType();
        final Object other$suiteType = other.getSuiteType();
        if (this$suiteType == null) {
            if (other$suiteType == null) {
                return true;
            }
        }
        else if (this$suiteType.equals(other$suiteType)) {
            return true;
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof SysDataAcsry;
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
        result = result * 59 + this.getBaseScore();
        result = result * 59 + this.getPos();
        result = result * 59 + this.getMaxEndure();
        result = result * 59 + this.getNeedGg();
        result = result * 59 + this.getNeedLx();
        result = result * 59 + this.getNeedLl();
        result = result * 59 + this.getNeedMj();
        result = result * 59 + this.getNeedGrade();
        result = result * 59 + this.getNeedRei();
        final Object $ownerRole = this.getOwnerRole();
        result = result * 59 + (($ownerRole == null) ? 43 : $ownerRole.hashCode());
        result = result * 59 + this.getSex();
        result = result * 59 + this.getRarity();
        result = result * 59 + this.getShape();
        final long $rndRange = Double.doubleToLongBits(this.getRndRange());
        result = result * 59 + (int)($rndRange >>> 32 ^ $rndRange);
        final long $basicPrice = this.getBasicPrice();
        result = result * 59 + (int)($basicPrice >>> 32 ^ $basicPrice);
        final Object $suiteType = this.getSuiteType();
        result = result * 59 + (($suiteType == null) ? 43 : $suiteType.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "SysDataAcsry(serial=" + this.getSerial() + ", name=" + this.getName() + ", baseAttr=" + this.getBaseAttr() + ", baseScore=" + this.getBaseScore() + ", pos=" + this.getPos() + ", maxEndure=" + this.getMaxEndure() + ", needGg=" + this.getNeedGg() + ", needLx=" + this.getNeedLx() + ", needLl=" + this.getNeedLl() + ", needMj=" + this.getNeedMj() + ", needGrade=" + this.getNeedGrade() + ", needRei=" + this.getNeedRei() + ", ownerRole=" + this.getOwnerRole() + ", sex=" + this.getSex() + ", rarity=" + this.getRarity() + ", shape=" + this.getShape() + ", rndRange=" + this.getRndRange() + ", basicPrice=" + this.getBasicPrice() + ", suiteType=" + this.getSuiteType() + ")";
    }
}
