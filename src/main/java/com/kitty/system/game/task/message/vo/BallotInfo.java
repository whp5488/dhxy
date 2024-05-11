
package com.kitty.system.game.task.message.vo;

import java.beans.ConstructorProperties;

public class BallotInfo
{
    private int serial;
    private String name;
    private short type;
    
    public int getSerial() {
        return this.serial;
    }
    
    public String getName() {
        return this.name;
    }
    
    public short getType() {
        return this.type;
    }
    
    public void setSerial(final int serial) {
        this.serial = serial;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public void setType(final short type) {
        this.type = type;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof BallotInfo)) {
            return false;
        }
        final BallotInfo other = (BallotInfo)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getSerial() != other.getSerial()) {
            return false;
        }
        final Object this$name = this.getName();
        final Object other$name = other.getName();
        if (this$name == null) {
            if (other$name == null) {
                return this.getType() == other.getType();
            }
        }
        else if (this$name.equals(other$name)) {
            return this.getType() == other.getType();
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof BallotInfo;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getSerial();
        final Object $name = this.getName();
        result = result * 59 + (($name == null) ? 43 : $name.hashCode());
        result = result * 59 + this.getType();
        return result;
    }
    
    @Override
    public String toString() {
        return "BallotInfo(serial=" + this.getSerial() + ", name=" + this.getName() + ", type=" + this.getType() + ")";
    }
    
    public BallotInfo() {
    }
    
    @ConstructorProperties({ "serial", "name", "type" })
    public BallotInfo(final int serial, final String name, final short type) {
        this.serial = serial;
        this.name = name;
        this.type = type;
    }
}
