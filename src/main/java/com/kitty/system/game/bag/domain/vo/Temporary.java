
package com.kitty.system.game.bag.domain.vo;

import java.beans.ConstructorProperties;
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
        final Temporary other = (Temporary)o;
        if (!other.canEqual(this)) {
            return false;
        }
        final Object this$id = this.getId();
        final Object other$id = other.getId();
        Label_0065: {
            if (this$id == null) {
                if (other$id == null) {
                    break Label_0065;
                }
            }
            else if (this$id.equals(other$id)) {
                break Label_0065;
            }
            return false;
        }
        final Object this$serial = this.getSerial();
        final Object other$serial = other.getSerial();
        Label_0102: {
            if (this$serial == null) {
                if (other$serial == null) {
                    break Label_0102;
                }
            }
            else if (this$serial.equals(other$serial)) {
                break Label_0102;
            }
            return false;
        }
        final Object this$type = this.getType();
        final Object other$type = other.getType();
        Label_0139: {
            if (this$type == null) {
                if (other$type == null) {
                    break Label_0139;
                }
            }
            else if (this$type.equals(other$type)) {
                break Label_0139;
            }
            return false;
        }
        final Object this$num = this.getNum();
        final Object other$num = other.getNum();
        Label_0176: {
            if (this$num == null) {
                if (other$num == null) {
                    break Label_0176;
                }
            }
            else if (this$num.equals(other$num)) {
                break Label_0176;
            }
            return false;
        }
        final Object this$rests = this.getRests();
        final Object other$rests = other.getRests();
        if (this$rests == null) {
            if (other$rests == null) {
                return true;
            }
        }
        else if (this$rests.equals(other$rests)) {
            return true;
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof Temporary;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $id = this.getId();
        result = result * 59 + (($id == null) ? 43 : $id.hashCode());
        final Object $serial = this.getSerial();
        result = result * 59 + (($serial == null) ? 43 : $serial.hashCode());
        final Object $type = this.getType();
        result = result * 59 + (($type == null) ? 43 : $type.hashCode());
        final Object $num = this.getNum();
        result = result * 59 + (($num == null) ? 43 : $num.hashCode());
        final Object $rests = this.getRests();
        result = result * 59 + (($rests == null) ? 43 : $rests.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "Temporary(id=" + this.getId() + ", serial=" + this.getSerial() + ", type=" + this.getType() + ", num=" + this.getNum() + ", rests=" + this.getRests() + ")";
    }
    
    @ConstructorProperties({ "id", "serial", "type", "num", "rests" })
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
