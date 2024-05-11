
package com.kitty.system.game.activity.message.vo;

public class MemberInfo
{
    private String name;
    private byte rei;
    private short level;
    private int serial;
    private int score;
    
    public MemberInfo() {
        this.rei = 0;
        this.score = 0;
    }
    
    public MemberInfo(final String name, final byte rei, final short level, final int serial, final int score) {
        this.rei = 0;
        this.score = 0;
        this.name = name;
        this.rei = rei;
        this.level = level;
        this.serial = serial;
        this.score = score;
    }
    
    public String getName() {
        return this.name;
    }
    
    public byte getRei() {
        return this.rei;
    }
    
    public short getLevel() {
        return this.level;
    }
    
    public int getSerial() {
        return this.serial;
    }
    
    public int getScore() {
        return this.score;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public void setRei(final byte rei) {
        this.rei = rei;
    }
    
    public void setLevel(final short level) {
        this.level = level;
    }
    
    public void setSerial(final int serial) {
        this.serial = serial;
    }
    
    public void setScore(final int score) {
        this.score = score;
    }
}
