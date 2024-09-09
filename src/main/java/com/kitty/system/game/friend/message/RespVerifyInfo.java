package com.kitty.system.game.friend.message;

import com.kitty.system.common.enter.FiedValue;
import java.util.List;
import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 41000)
public class RespVerifyInfo extends Message
{
    private String id;
    private String name;
    private byte rei;
    private short level;
    private int serial;
    private List<FiedValue> rests;
    
    public RespVerifyInfo() {
    }
    
    public RespVerifyInfo(String id, String name, byte rei, short level, int serial) {
        this.id = id;
        this.name = name;
        this.rei = rei;
        this.level = level;
        this.serial = serial;
    }
    
    public RespVerifyInfo(String id, String name, byte rei, short level, int serial, List<FiedValue> rests) {
        this.id = id;
        this.name = name;
        this.rei = rei;
        this.level = level;
        this.serial = serial;
        this.rests = rests;
    }
    
    public String getId() {
        return this.id;
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
    
    public List<FiedValue> getRests() {
        return this.rests;
    }
    
    public void setId(final String id) {
        this.id = id;
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
    
    public void setRests(final List<FiedValue> rests) {
        this.rests = rests;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof RespVerifyInfo)) {
            return false;
        }
        RespVerifyInfo other = (RespVerifyInfo)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$id = this.getId();
        Object other$id = other.getId();
        if (!((this$id != null) ? this$id.equals(other$id) : (other$id == null))) {
            return false;
        }
        Object this$name = this.getName();
        Object other$name = other.getName();
        if (!((this$name != null) ? this$name.equals(other$name) : (other$name == null))) {
            return false;
        }
        if (this.getRei() != other.getRei()) {
            return false;
        }
        if (this.getLevel() != other.getLevel()) {
            return false;
        }
        if (this.getSerial() != other.getSerial()) {
            return false;
        }
        Object this$rests = this.getRests();
        Object other$rests = other.getRests();
        return (this$rests != null) ? this$rests.equals(other$rests) : (other$rests == null);
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof RespVerifyInfo;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Object $id = this.getId();
        result = result * 59 + (($id == null) ? 43 : $id.hashCode());
        Object $name = this.getName();
        result = result * 59 + (($name == null) ? 43 : $name.hashCode());
        result = result * 59 + this.getRei();
        result = result * 59 + this.getLevel();
        result = result * 59 + this.getSerial();
        Object $rests = this.getRests();
        result = result * 59 + (($rests == null) ? 43 : $rests.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "RespVerifyInfo(id=" + this.getId() + ", name=" + this.getName() + ", rei=" + (int)this.getRei() + ", level=" + (int)this.getLevel() + ", serial=" + this.getSerial() + ", rests=" + this.getRests() + ")";
    }
}
