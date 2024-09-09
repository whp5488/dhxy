package com.kitty.system.game.activity.model.user.hg;

import java.io.Serializable;

public class HgPrepare implements Serializable
{
    private int serial;
    private byte rei;
    private short grade;
    private String name;
    private byte state;
    
    public int getSerial() {
        return this.serial;
    }
    
    public byte getRei() {
        return this.rei;
    }
    
    public short getGrade() {
        return this.grade;
    }
    
    public String getName() {
        return this.name;
    }
    
    public byte getState() {
        return this.state;
    }
    
    public void setSerial(final int serial) {
        this.serial = serial;
    }
    
    public void setRei(final byte rei) {
        this.rei = rei;
    }
    
    public void setGrade(final short grade) {
        this.grade = grade;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public void setState(final byte state) {
        this.state = state;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof HgPrepare)) {
            return false;
        }
        HgPrepare other = (HgPrepare)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getSerial() != other.getSerial()) {
            return false;
        }
        if (this.getRei() != other.getRei()) {
            return false;
        }
        if (this.getGrade() != other.getGrade()) {
            return false;
        }
        Object this$name = this.getName();
        Object other$name = other.getName();
        return ((this$name != null) ? this$name.equals(other$name) : (other$name == null)) && this.getState() == other.getState();
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof HgPrepare;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getSerial();
        result = result * 59 + this.getRei();
        result = result * 59 + this.getGrade();
        Object $name = this.getName();
        result = result * 59 + (($name == null) ? 43 : $name.hashCode());
        result = result * 59 + this.getState();
        return result;
    }
    
    @Override
    public String toString() {
        return "HgPrepare(serial=" + this.getSerial() + ", rei=" + (int)this.getRei() + ", grade=" + (int)this.getGrade() + ", name=" + this.getName() + ", state=" + (int)this.getState() + ")";
    }
    
    public HgPrepare(final int serial, final byte rei, final short grade, final String name, final byte state) {
        this.state = 0;
        this.serial = serial;
        this.rei = rei;
        this.grade = grade;
        this.name = name;
        this.state = state;
    }
    
    public HgPrepare() {
        this.state = 0;
    }
}
