package com.kitty.system.init.domain.vo;

import java.io.Serializable;

public class MonsterParameter implements Serializable
{
    private Integer serial;
    private double percent;
    private String type;
    private byte round;
    
    public MonsterParameter() {
    }
    
    public Integer getSerial() {
        return this.serial;
    }
    
    public double getPercent() {
        return this.percent;
    }
    
    public String getType() {
        return this.type;
    }
    
    public byte getRound() {
        return this.round;
    }
    
    public void setSerial(final Integer serial) {
        this.serial = serial;
    }
    
    public void setPercent(final double percent) {
        this.percent = percent;
    }
    
    public void setType(final String type) {
        this.type = type;
    }
    
    public void setRound(final byte round) {
        this.round = round;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof MonsterParameter)) {
            return false;
        }
        MonsterParameter other = (MonsterParameter)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$serial = this.getSerial();
        Object other$serial = other.getSerial();
        if (!((this$serial != null) ? this$serial.equals(other$serial) : (other$serial == null))) {
            return false;
        }
        if (Double.compare(this.getPercent(), other.getPercent()) != 0) {
            return false;
        }
        Object this$type = this.getType();
        Object other$type = other.getType();
        return ((this$type != null) ? this$type.equals(other$type) : (other$type == null)) && this.getRound() == other.getRound();
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof MonsterParameter;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Object $serial = this.getSerial();
        result = result * 59 + (($serial == null) ? 43 : $serial.hashCode());
        long $percent = Double.doubleToLongBits(this.getPercent());
        result = result * 59 + (int)($percent >>> 32 ^ $percent);
        Object $type = this.getType();
        result = result * 59 + (($type == null) ? 43 : $type.hashCode());
        result = result * 59 + this.getRound();
        return result;
    }
    
    @Override
    public String toString() {
        return "MonsterParameter(serial=" + this.getSerial() + ", percent=" + this.getPercent() + ", type=" + this.getType() + ", round=" + (int)this.getRound() + ")";
    }
}
