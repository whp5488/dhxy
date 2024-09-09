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
    
    public EquipAlike() {
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
}
