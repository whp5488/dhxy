package com.kitty.system.game.bag.domain.vo;

import java.io.Serializable;

public class Temporary implements Serializable
{
    private String id;
    private Integer serial;
    private Integer type;
    private Integer num;
    private Integer rests;
    
    public String getId() {
        return this.id;
    }
    
    public Integer getSerial() {
        return this.serial;
    }
    
    public Integer getType() {
        return this.type;
    }
    
    public Integer getNum() {
        return this.num;
    }
    
    public Integer getRests() {
        return this.rests;
    }
    
    public void setId(final String id) {
        this.id = id;
    }
    
    public void setSerial(final Integer serial) {
        this.serial = serial;
    }
    
    public void setType(final Integer type) {
        this.type = type;
    }
    
    public void setNum(final Integer num) {
        this.num = num;
    }
    
    public void setRests(final Integer rests) {
        this.rests = rests;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Temporary)) {
            return false;
        }
        Temporary other = (Temporary)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$id = this.getId();
        Object other$id = other.getId();
        if (!((this$id != null) ? this$id.equals(other$id) : (other$id == null))) {
            return false;
        }
        Object this$serial = this.getSerial();
        Object other$serial = other.getSerial();
        if (!((this$serial != null) ? this$serial.equals(other$serial) : (other$serial == null))) {
            return false;
        }
        Object this$type = this.getType();
        Object other$type = other.getType();
        if (!((this$type != null) ? this$type.equals(other$type) : (other$type == null))) {
            return false;
        }
        Object this$num = this.getNum();
        Object other$num = other.getNum();
        if (!((this$num != null) ? this$num.equals(other$num) : (other$num == null))) {
            return false;
        }
        Object this$rests = this.getRests();
        Object other$rests = other.getRests();
        return (this$rests != null) ? this$rests.equals(other$rests) : (other$rests == null);
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof Temporary;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Object $id = this.getId();
        result = result * 59 + (($id == null) ? 43 : $id.hashCode());
        Object $serial = this.getSerial();
        result = result * 59 + (($serial == null) ? 43 : $serial.hashCode());
        Object $type = this.getType();
        result = result * 59 + (($type == null) ? 43 : $type.hashCode());
        Object $num = this.getNum();
        result = result * 59 + (($num == null) ? 43 : $num.hashCode());
        Object $rests = this.getRests();
        result = result * 59 + (($rests == null) ? 43 : $rests.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "Temporary(id=" + this.getId() + ", serial=" + this.getSerial() + ", type=" + this.getType() + ", num=" + this.getNum() + ", rests=" + this.getRests() + ")";
    }
    
    public Temporary(final String id, final Integer serial, final Integer type, final Integer num, final Integer rests) {
        this.id = id;
        this.serial = serial;
        this.type = type;
        this.num = num;
        this.rests = rests;
    }
    
    public Temporary() {
    }
}
