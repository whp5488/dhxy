
package com.kitty.system.game.card.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 65524)
public class RespCardFlyInfo extends Message
{
    private String id;
    private int serial;
    private String rests;
    
    public RespCardFlyInfo() {
    }
    
    public RespCardFlyInfo(final String id, final int serial, final String rests) {
        this.id = id;
        this.serial = serial;
        this.rests = rests;
    }
    
    public String getId() {
        return this.id;
    }
    
    public int getSerial() {
        return this.serial;
    }
    
    public String getRests() {
        return this.rests;
    }
    
    public void setId(final String id) {
        this.id = id;
    }
    
    public void setSerial(final int serial) {
        this.serial = serial;
    }
    
    public void setRests(final String rests) {
        this.rests = rests;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof RespCardFlyInfo)) {
            return false;
        }
        final RespCardFlyInfo other = (RespCardFlyInfo)o;
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
        return other instanceof RespCardFlyInfo;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $id = this.getId();
        result = result * 59 + (($id == null) ? 43 : $id.hashCode());
        result = result * 59 + this.getSerial();
        final Object $rests = this.getRests();
        result = result * 59 + (($rests == null) ? 43 : $rests.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "RespCardFlyInfo(id=" + this.getId() + ", serial=" + this.getSerial() + ", rests=" + this.getRests() + ")";
    }
}
