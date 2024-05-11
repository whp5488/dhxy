
package com.kitty.system.game.task.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 16007)
public class RespTaskIcon extends Message
{
    private int serial;
    private String para;
    private byte is;
    
    public RespTaskIcon() {
        this.serial = 0;
        this.is = 1;
    }
    
    public RespTaskIcon(final int serial, final String para, final byte is) {
        this.serial = 0;
        this.is = 1;
        this.serial = serial;
        this.para = para;
        this.is = is;
    }
    
    public int getSerial() {
        return this.serial;
    }
    
    public String getPara() {
        return this.para;
    }
    
    public byte getIs() {
        return this.is;
    }
    
    public void setSerial(final int serial) {
        this.serial = serial;
    }
    
    public void setPara(final String para) {
        this.para = para;
    }
    
    public void setIs(final byte is) {
        this.is = is;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof RespTaskIcon)) {
            return false;
        }
        final RespTaskIcon other = (RespTaskIcon)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getSerial() != other.getSerial()) {
            return false;
        }
        final Object this$para = this.getPara();
        final Object other$para = other.getPara();
        if (this$para == null) {
            if (other$para == null) {
                return this.getIs() == other.getIs();
            }
        }
        else if (this$para.equals(other$para)) {
            return this.getIs() == other.getIs();
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof RespTaskIcon;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getSerial();
        final Object $para = this.getPara();
        result = result * 59 + (($para == null) ? 43 : $para.hashCode());
        result = result * 59 + this.getIs();
        return result;
    }
    
    @Override
    public String toString() {
        return "RespTaskIcon(serial=" + this.getSerial() + ", para=" + this.getPara() + ", is=" + this.getIs() + ")";
    }
}
