
package com.kitty.system.game.equip.domain.vo;

import java.util.List;
import java.io.Serializable;

public class EquipAlike implements Serializable
{
    private String name;
    private int serial;
    private String baseAttr;
    private int baseScore;
    private int pos;
    private int maxEmbedGemCnt;
    private int maxEndure;
    private int needGg;
    private int needLx;
    private int needLl;
    private int needMj;
    private int needGrade;
    private int needRei;
    private int ownerRole;
    private List<Integer> race;
    private int rarity;
    private String rndWeight;
    private int sex;
    private int shape;
    private int canMerge;
    
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
    
    public int getOwnerRole() {
        return this.ownerRole;
    }
    
    public List<Integer> getRace() {
        return this.race;
    }
    
    public int getRarity() {
        return this.rarity;
    }
    
    public String getRndWeight() {
        return this.rndWeight;
    }
    
    public int getSex() {
        return this.sex;
    }
    
    public int getShape() {
        return this.shape;
    }
    
    public int getCanMerge() {
        return this.canMerge;
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
    
    public void setOwnerRole(final int ownerRole) {
        this.ownerRole = ownerRole;
    }
    
    public void setRace(final List<Integer> race) {
        this.race = race;
    }
    
    public void setRarity(final int rarity) {
        this.rarity = rarity;
    }
    
    public void setRndWeight(final String rndWeight) {
        this.rndWeight = rndWeight;
    }
    
    public void setSex(final int sex) {
        this.sex = sex;
    }
    
    public void setShape(final int shape) {
        this.shape = shape;
    }
    
    public void setCanMerge(final int canMerge) {
        this.canMerge = canMerge;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof EquipAlike)) {
            return false;
        }
        final EquipAlike other = (EquipAlike)o;
        if (!other.canEqual(this)) {
            return false;
        }
        final Object this$name = this.getName();
        final Object other$name = other.getName();
        Label_0065: {
            if (this$name == null) {
                if (other$name == null) {
                    break Label_0065;
                }
            }
            else if (this$name.equals(other$name)) {
                break Label_0065;
            }
            return false;
        }
        if (this.getSerial() != other.getSerial()) {
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
        if (this.getOwnerRole() != other.getOwnerRole()) {
            return false;
        }
        final Object this$race = this.getRace();
        final Object other$race = other.getRace();
        Label_0295: {
            if (this$race == null) {
                if (other$race == null) {
                    break Label_0295;
                }
            }
            else if (this$race.equals(other$race)) {
                break Label_0295;
            }
            return false;
        }
        if (this.getRarity() != other.getRarity()) {
            return false;
        }
        final Object this$rndWeight = this.getRndWeight();
        final Object other$rndWeight = other.getRndWeight();
        if (this$rndWeight == null) {
            if (other$rndWeight == null) {
                return this.getSex() == other.getSex() && this.getShape() == other.getShape() && this.getCanMerge() == other.getCanMerge();
            }
        }
        else if (this$rndWeight.equals(other$rndWeight)) {
            return this.getSex() == other.getSex() && this.getShape() == other.getShape() && this.getCanMerge() == other.getCanMerge();
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof EquipAlike;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $name = this.getName();
        result = result * 59 + (($name == null) ? 43 : $name.hashCode());
        result = result * 59 + this.getSerial();
        final Object $baseAttr = this.getBaseAttr();
        result = result * 59 + (($baseAttr == null) ? 43 : $baseAttr.hashCode());
        result = result * 59 + this.getBaseScore();
        result = result * 59 + this.getPos();
        result = result * 59 + this.getMaxEmbedGemCnt();
        result = result * 59 + this.getMaxEndure();
        result = result * 59 + this.getNeedGg();
        result = result * 59 + this.getNeedLx();
        result = result * 59 + this.getNeedLl();
        result = result * 59 + this.getNeedMj();
        result = result * 59 + this.getNeedGrade();
        result = result * 59 + this.getNeedRei();
        result = result * 59 + this.getOwnerRole();
        final Object $race = this.getRace();
        result = result * 59 + (($race == null) ? 43 : $race.hashCode());
        result = result * 59 + this.getRarity();
        final Object $rndWeight = this.getRndWeight();
        result = result * 59 + (($rndWeight == null) ? 43 : $rndWeight.hashCode());
        result = result * 59 + this.getSex();
        result = result * 59 + this.getShape();
        result = result * 59 + this.getCanMerge();
        return result;
    }
    
    @Override
    public String toString() {
        return "EquipAlike(name=" + this.getName() + ", serial=" + this.getSerial() + ", baseAttr=" + this.getBaseAttr() + ", baseScore=" + this.getBaseScore() + ", pos=" + this.getPos() + ", maxEmbedGemCnt=" + this.getMaxEmbedGemCnt() + ", maxEndure=" + this.getMaxEndure() + ", needGg=" + this.getNeedGg() + ", needLx=" + this.getNeedLx() + ", needLl=" + this.getNeedLl() + ", needMj=" + this.getNeedMj() + ", needGrade=" + this.getNeedGrade() + ", needRei=" + this.getNeedRei() + ", ownerRole=" + this.getOwnerRole() + ", race=" + this.getRace() + ", rarity=" + this.getRarity() + ", rndWeight=" + this.getRndWeight() + ", sex=" + this.getSex() + ", shape=" + this.getShape() + ", canMerge=" + this.getCanMerge() + ")";
    }
}
