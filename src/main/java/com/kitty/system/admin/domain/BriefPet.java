
package com.kitty.system.admin.domain;

import java.io.Serializable;

public class BriefPet implements Serializable
{
    private int id;
    private int serial;
    private int rei;
    private int grade;
    private int hp;
    private int mp;
    private int ap;
    private int sp;
    private int growUp;
    private long close;
    private short awaken;
    private String skills;
    
    public BriefPet() {
        this.growUp = 0;
        this.close = 0L;
        this.awaken = 0;
    }
    
    public int getId() {
        return this.id;
    }
    
    public void setId(final int id) {
        this.id = id;
    }
    
    public int getSerial() {
        return this.serial;
    }
    
    public void setSerial(final int serial) {
        this.serial = serial;
    }
    
    public int getRei() {
        return this.rei;
    }
    
    public void setRei(final int rei) {
        this.rei = rei;
    }
    
    public int getGrade() {
        return this.grade;
    }
    
    public void setGrade(final int grade) {
        this.grade = grade;
    }
    
    public int getHp() {
        return this.hp;
    }
    
    public void setHp(final int hp) {
        this.hp = hp;
    }
    
    public int getMp() {
        return this.mp;
    }
    
    public void setMp(final int mp) {
        this.mp = mp;
    }
    
    public int getAp() {
        return this.ap;
    }
    
    public void setAp(final int ap) {
        this.ap = ap;
    }
    
    public int getSp() {
        return this.sp;
    }
    
    public void setSp(final int sp) {
        this.sp = sp;
    }
    
    public int getGrowUp() {
        return this.growUp;
    }
    
    public void setGrowUp(final int growUp) {
        this.growUp = growUp;
    }
    
    public long getClose() {
        return this.close;
    }
    
    public void setClose(final long close) {
        this.close = close;
    }
    
    public short getAwaken() {
        return this.awaken;
    }
    
    public void setAwaken(final short awaken) {
        this.awaken = awaken;
    }
    
    public String getSkills() {
        return this.skills;
    }
    
    public void setSkills(final String skills) {
        this.skills = skills;
    }
}
