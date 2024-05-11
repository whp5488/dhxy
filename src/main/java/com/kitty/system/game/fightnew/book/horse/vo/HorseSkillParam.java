
package com.kitty.system.game.fightnew.book.horse.vo;

import java.beans.ConstructorProperties;
import java.io.Serializable;

public class HorseSkillParam implements Serializable
{
    private int serial;
    private long proficient;
    private int growUp;
    private short level;
    
    public void setSerial(final int serial) {
        this.serial = serial;
    }
    
    public void setProficient(final long proficient) {
        this.proficient = proficient;
    }
    
    public void setGrowUp(final int growUp) {
        this.growUp = growUp;
    }
    
    public void setLevel(final short level) {
        this.level = level;
    }
    
    public int getSerial() {
        return this.serial;
    }
    
    public long getProficient() {
        return this.proficient;
    }
    
    public int getGrowUp() {
        return this.growUp;
    }
    
    public short getLevel() {
        return this.level;
    }
    
    @ConstructorProperties({ "serial", "proficient", "growUp", "level" })
    public HorseSkillParam(final int serial, final long proficient, final int growUp, final short level) {
        this.serial = serial;
        this.proficient = proficient;
        this.growUp = growUp;
        this.level = level;
    }
    
    public HorseSkillParam() {
    }
}
