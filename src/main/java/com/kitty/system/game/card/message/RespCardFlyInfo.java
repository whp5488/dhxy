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
    
    public RespCardFlyInfo(String id, int serial, String rests) {
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
        RespCardFlyInfo other = (RespCardFlyInfo)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$id = this.getId();
        Object other$id = other.getId();
        if (!((this$id != null) ? this$id.equals(other$id) : (other$id == null))) {
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
        return other instanceof RespCardFlyInfo;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Object $id = this.getId();
        result = result * 59 + (($id == null) ? 43 : $id.hashCode());
        result = result * 59 + this.getSerial();
        Object $rests = this.getRests();
        result = result * 59 + (($rests == null) ? 43 : $rests.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "RespCardFlyInfo(id=" + this.getId() + ", serial=" + this.getSerial() + ", rests=" + this.getRests() + ")";
    }
}
