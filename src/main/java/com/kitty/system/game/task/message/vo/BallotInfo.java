package com.kitty.system.game.task.message.vo;

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
        BallotInfo other = (BallotInfo)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getSerial() != other.getSerial()) {
            return false;
        }
        Object this$name = this.getName();
        Object other$name = other.getName();
        return ((this$name != null) ? this$name.equals(other$name) : (other$name == null)) && this.getType() == other.getType();
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof BallotInfo;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getSerial();
        Object $name = this.getName();
        result = result * 59 + (($name == null) ? 43 : $name.hashCode());
        result = result * 59 + this.getType();
        return result;
    }
    
    @Override
    public String toString() {
        return "BallotInfo(serial=" + this.getSerial() + ", name=" + this.getName() + ", type=" + (int)this.getType() + ")";
    }
    
    public BallotInfo() {
    }
    
    public BallotInfo(final int serial, final String name, final short type) {
        this.serial = serial;
        this.name = name;
        this.type = type;
    }
}
