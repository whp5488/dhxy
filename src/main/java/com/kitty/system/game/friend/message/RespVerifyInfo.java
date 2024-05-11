
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
    
    public RespVerifyInfo(final String id, final String name, final byte rei, final short level, final int serial) {
        this.id = id;
        this.name = name;
        this.rei = rei;
        this.level = level;
        this.serial = serial;
    }
    
    public RespVerifyInfo(final String id, final String name, final byte rei, final short level, final int serial, final List<FiedValue> rests) {
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
        final RespVerifyInfo other = (RespVerifyInfo)o;
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
        final Object this$name = this.getName();
        final Object other$name = other.getName();
        Label_0102: {
            if (this$name == null) {
                if (other$name == null) {
                    break Label_0102;
                }
            }
            else if (this$name.equals(other$name)) {
                break Label_0102;
            }
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
        return other instanceof RespVerifyInfo;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $id = this.getId();
        result = result * 59 + (($id == null) ? 43 : $id.hashCode());
        final Object $name = this.getName();
        result = result * 59 + (($name == null) ? 43 : $name.hashCode());
        result = result * 59 + this.getRei();
        result = result * 59 + this.getLevel();
        result = result * 59 + this.getSerial();
        final Object $rests = this.getRests();
        result = result * 59 + (($rests == null) ? 43 : $rests.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "RespVerifyInfo(id=" + this.getId() + ", name=" + this.getName() + ", rei=" + this.getRei() + ", level=" + this.getLevel() + ", serial=" + this.getSerial() + ", rests=" + this.getRests() + ")";
    }
}
