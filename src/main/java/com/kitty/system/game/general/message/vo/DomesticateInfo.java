
package com.kitty.system.game.general.message.vo;

import java.beans.ConstructorProperties;
import java.io.Serializable;

public class DomesticateInfo implements Serializable
{
    private String id;
    private int time;
    private int num;
    private short residue;
    private byte type;
    
    public String getId() {
        return this.id;
    }
    
    public int getTime() {
        return this.time;
    }
    
    public int getNum() {
        return this.num;
    }
    
    public short getResidue() {
        return this.residue;
    }
    
    public byte getType() {
        return this.type;
    }
    
    public void setId(final String id) {
        this.id = id;
    }
    
    public void setTime(final int time) {
        this.time = time;
    }
    
    public void setNum(final int num) {
        this.num = num;
    }
    
    public void setResidue(final short residue) {
        this.residue = residue;
    }
    
    public void setType(final byte type) {
        this.type = type;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof DomesticateInfo)) {
            return false;
        }
        final DomesticateInfo other = (DomesticateInfo)o;
        if (!other.canEqual(this)) {
            return false;
        }
        final Object this$id = this.getId();
        final Object other$id = other.getId();
        if (this$id == null) {
            if (other$id == null) {
                return this.getTime() == other.getTime() && this.getNum() == other.getNum() && this.getResidue() == other.getResidue() && this.getType() == other.getType();
            }
        }
        else if (this$id.equals(other$id)) {
            return this.getTime() == other.getTime() && this.getNum() == other.getNum() && this.getResidue() == other.getResidue() && this.getType() == other.getType();
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof DomesticateInfo;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $id = this.getId();
        result = result * 59 + (($id == null) ? 43 : $id.hashCode());
        result = result * 59 + this.getTime();
        result = result * 59 + this.getNum();
        result = result * 59 + this.getResidue();
        result = result * 59 + this.getType();
        return result;
    }
    
    @Override
    public String toString() {
        return "DomesticateInfo(id=" + this.getId() + ", time=" + this.getTime() + ", num=" + this.getNum() + ", residue=" + this.getResidue() + ", type=" + this.getType() + ")";
    }
    
    @ConstructorProperties({ "id", "time", "num", "residue", "type" })
    public DomesticateInfo(final String id, final int time, final int num, final short residue, final byte type) {
        this.residue = -1;
        this.id = id;
        this.time = time;
        this.num = num;
        this.residue = residue;
        this.type = type;
    }
    
    public DomesticateInfo() {
        this.residue = -1;
    }
}
