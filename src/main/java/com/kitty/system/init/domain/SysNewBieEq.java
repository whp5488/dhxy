package com.kitty.system.init.domain;

import org.nutz.dao.entity.annotation.Default;
import java.util.List;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Table;
import java.io.Serializable;

@Table("new_bie_eq")
public class SysNewBieEq implements Serializable
{
    @Column("id")
    private int id;
    @Column("name")
    private String name;
    @Column("serial")
    private int serial;
    @Column("base_attr")
    private String baseAttr;
    @Column("base_score")
    private int baseScore;
    @Column("desc")
    private String desc;
    @Column("pos")
    private int pos;
    @Column("max_embed_gem_cnt")
    private int maxEmbedGemCnt;
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
    @Column("overlap")
    private int overlap;
    @Column("owner_role")
    @Default("[]")
    private List<Integer> ownerRole;
    @Column("race")
    private int race;
    @Column("sex")
    private int sex;
    @Column("shape")
    private int shape;
    @Column("price")
    private int price;
    @Column("can_merge")
    private int canMerge;
    
    public SysNewBieEq() {
    }
    
    public int getId() {
        return this.id;
    }
    
    public String getName() {
        return this.name;
    }
    
    public int getSerial() {
        return this.serial;
    }
    
    public String getBaseAttr() {
        return this.baseAttr;
    }
    
    public int getBaseScore() {
        return this.baseScore;
    }
    
    public String getDesc() {
        return this.desc;
    }
    
    public int getPos() {
        return this.pos;
    }
    
    public int getMaxEmbedGemCnt() {
        return this.maxEmbedGemCnt;
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
    
    public int getOverlap() {
        return this.overlap;
    }
    
    public List<Integer> getOwnerRole() {
        return this.ownerRole;
    }
    
    public int getRace() {
        return this.race;
    }
    
    public int getSex() {
        return this.sex;
    }
    
    public int getShape() {
        return this.shape;
    }
    
    public int getPrice() {
        return this.price;
    }
    
    public int getCanMerge() {
        return this.canMerge;
    }
    
    public void setId(final int id) {
        this.id = id;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public void setSerial(final int serial) {
        this.serial = serial;
    }
    
    public void setBaseAttr(final String baseAttr) {
        this.baseAttr = baseAttr;
    }
    
    public void setBaseScore(final int baseScore) {
        this.baseScore = baseScore;
    }
    
    public void setDesc(final String desc) {
        this.desc = desc;
    }
    
    public void setPos(final int pos) {
        this.pos = pos;
    }
    
    public void setMaxEmbedGemCnt(final int maxEmbedGemCnt) {
        this.maxEmbedGemCnt = maxEmbedGemCnt;
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
    
    public void setOverlap(final int overlap) {
        this.overlap = overlap;
    }
    
    public void setOwnerRole(final List<Integer> ownerRole) {
        this.ownerRole = ownerRole;
    }
    
    public void setRace(final int race) {
        this.race = race;
    }
    
    public void setSex(final int sex) {
        this.sex = sex;
    }
    
    public void setShape(final int shape) {
        this.shape = shape;
    }
    
    public void setPrice(final int price) {
        this.price = price;
    }
    
    public void setCanMerge(final int canMerge) {
        this.canMerge = canMerge;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SysNewBieEq)) {
            return false;
        }
        SysNewBieEq other = (SysNewBieEq)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getId() != other.getId()) {
            return false;
        }
        Object this$name = this.getName();
        Object other$name = other.getName();
        if (!((this$name != null) ? this$name.equals(other$name) : (other$name == null))) {
            return false;
        }
        if (this.getSerial() != other.getSerial()) {
            return false;
        }
        Object this$baseAttr = this.getBaseAttr();
        Object other$baseAttr = other.getBaseAttr();
        if (!((this$baseAttr != null) ? this$baseAttr.equals(other$baseAttr) : (other$baseAttr == null))) {
            return false;
        }
        if (this.getBaseScore() != other.getBaseScore()) {
            return false;
        }
        Object this$desc = this.getDesc();
        Object other$desc = other.getDesc();
        if (!((this$desc != null) ? this$desc.equals(other$desc) : (other$desc == null))) {
            return false;
        }
        if (this.getPos() != other.getPos()) {
            return false;
        }
        if (this.getMaxEmbedGemCnt() != other.getMaxEmbedGemCnt()) {
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
        if (this.getOverlap() != other.getOverlap()) {
            return false;
        }
        Object this$ownerRole = this.getOwnerRole();
        Object other$ownerRole = other.getOwnerRole();
        return ((this$ownerRole != null) ? this$ownerRole.equals(other$ownerRole) : (other$ownerRole == null)) && this.getRace() == other.getRace() && this.getSex() == other.getSex() && this.getShape() == other.getShape() && this.getPrice() == other.getPrice() && this.getCanMerge() == other.getCanMerge();
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof SysNewBieEq;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getId();
        Object $name = this.getName();
        result = result * 59 + (($name == null) ? 43 : $name.hashCode());
        result = result * 59 + this.getSerial();
        Object $baseAttr = this.getBaseAttr();
        result = result * 59 + (($baseAttr == null) ? 43 : $baseAttr.hashCode());
        result = result * 59 + this.getBaseScore();
        Object $desc = this.getDesc();
        result = result * 59 + (($desc == null) ? 43 : $desc.hashCode());
        result = result * 59 + this.getPos();
        result = result * 59 + this.getMaxEmbedGemCnt();
        result = result * 59 + this.getMaxEndure();
        result = result * 59 + this.getNeedGg();
        result = result * 59 + this.getNeedLx();
        result = result * 59 + this.getNeedLl();
        result = result * 59 + this.getNeedMj();
        result = result * 59 + this.getNeedGrade();
        result = result * 59 + this.getNeedRei();
        result = result * 59 + this.getOverlap();
        Object $ownerRole = this.getOwnerRole();
        result = result * 59 + (($ownerRole == null) ? 43 : $ownerRole.hashCode());
        result = result * 59 + this.getRace();
        result = result * 59 + this.getSex();
        result = result * 59 + this.getShape();
        result = result * 59 + this.getPrice();
        result = result * 59 + this.getCanMerge();
        return result;
    }
    
    @Override
    public String toString() {
        return "SysNewBieEq(id=" + this.getId() + ", name=" + this.getName() + ", serial=" + this.getSerial() + ", baseAttr=" + this.getBaseAttr() + ", baseScore=" + this.getBaseScore() + ", desc=" + this.getDesc() + ", pos=" + this.getPos() + ", maxEmbedGemCnt=" + this.getMaxEmbedGemCnt() + ", maxEndure=" + this.getMaxEndure() + ", needGg=" + this.getNeedGg() + ", needLx=" + this.getNeedLx() + ", needLl=" + this.getNeedLl() + ", needMj=" + this.getNeedMj() + ", needGrade=" + this.getNeedGrade() + ", needRei=" + this.getNeedRei() + ", overlap=" + this.getOverlap() + ", ownerRole=" + this.getOwnerRole() + ", race=" + this.getRace() + ", sex=" + this.getSex() + ", shape=" + this.getShape() + ", price=" + this.getPrice() + ", canMerge=" + this.getCanMerge() + ")";
    }
}
