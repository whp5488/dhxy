
package com.kitty.record.domain;

import java.beans.ConstructorProperties;
import java.io.Serializable;

public class Record implements Serializable
{
    private long uid;
    private int roleId;
    private short type;
    private byte classify;
    private String string;
    private long time;
    private String marking;
    
    public long getUid() {
        return this.uid;
    }
    
    public int getRoleId() {
        return this.roleId;
    }
    
    public short getType() {
        return this.type;
    }
    
    public byte getClassify() {
        return this.classify;
    }
    
    public String getString() {
        return this.string;
    }
    
    public long getTime() {
        return this.time;
    }
    
    public String getMarking() {
        return this.marking;
    }
    
    public void setUid(final long uid) {
        this.uid = uid;
    }
    
    public void setRoleId(final int roleId) {
        this.roleId = roleId;
    }
    
    public void setType(final short type) {
        this.type = type;
    }
    
    public void setClassify(final byte classify) {
        this.classify = classify;
    }
    
    public void setString(final String string) {
        this.string = string;
    }
    
    public void setTime(final long time) {
        this.time = time;
    }
    
    public void setMarking(final String marking) {
        this.marking = marking;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Record)) {
            return false;
        }
        final Record other = (Record)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getUid() != other.getUid()) {
            return false;
        }
        if (this.getRoleId() != other.getRoleId()) {
            return false;
        }
        if (this.getType() != other.getType()) {
            return false;
        }
        if (this.getClassify() != other.getClassify()) {
            return false;
        }
        final Object this$string = this.getString();
        final Object other$string = other.getString();
        Label_0118: {
            if (this$string == null) {
                if (other$string == null) {
                    break Label_0118;
                }
            }
            else if (this$string.equals(other$string)) {
                break Label_0118;
            }
            return false;
        }
        if (this.getTime() != other.getTime()) {
            return false;
        }
        final Object this$marking = this.getMarking();
        final Object other$marking = other.getMarking();
        if (this$marking == null) {
            if (other$marking == null) {
                return true;
            }
        }
        else if (this$marking.equals(other$marking)) {
            return true;
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof Record;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final long $uid = this.getUid();
        result = result * 59 + (int)($uid >>> 32 ^ $uid);
        result = result * 59 + this.getRoleId();
        result = result * 59 + this.getType();
        result = result * 59 + this.getClassify();
        final Object $string = this.getString();
        result = result * 59 + (($string == null) ? 43 : $string.hashCode());
        final long $time = this.getTime();
        result = result * 59 + (int)($time >>> 32 ^ $time);
        final Object $marking = this.getMarking();
        result = result * 59 + (($marking == null) ? 43 : $marking.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "Record(uid=" + this.getUid() + ", roleId=" + this.getRoleId() + ", type=" + this.getType() + ", classify=" + this.getClassify() + ", string=" + this.getString() + ", time=" + this.getTime() + ", marking=" + this.getMarking() + ")";
    }
    
    @ConstructorProperties({ "uid", "roleId", "type", "classify", "string", "time", "marking" })
    public Record(final long uid, final int roleId, final short type, final byte classify, final String string, final long time, final String marking) {
        this.uid = uid;
        this.roleId = roleId;
        this.type = type;
        this.classify = classify;
        this.string = string;
        this.time = time;
        this.marking = marking;
    }
    
    public Record() {
    }
}
