
package com.kitty.system.game.general.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 8854)
public class RespRectifyTime extends Message
{
    private String reqTime;
    private String respTime;
    
    public RespRectifyTime() {
        this.respTime = String.valueOf(System.currentTimeMillis());
    }
    
    public RespRectifyTime(final String reqTime) {
        this.respTime = String.valueOf(System.currentTimeMillis());
        this.reqTime = reqTime;
    }
    
    public String getReqTime() {
        return this.reqTime;
    }
    
    public String getRespTime() {
        return this.respTime;
    }
    
    public void setReqTime(final String reqTime) {
        this.reqTime = reqTime;
    }
    
    public void setRespTime(final String respTime) {
        this.respTime = respTime;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof RespRectifyTime)) {
            return false;
        }
        final RespRectifyTime other = (RespRectifyTime)o;
        if (!other.canEqual(this)) {
            return false;
        }
        final Object this$reqTime = this.getReqTime();
        final Object other$reqTime = other.getReqTime();
        Label_0065: {
            if (this$reqTime == null) {
                if (other$reqTime == null) {
                    break Label_0065;
                }
            }
            else if (this$reqTime.equals(other$reqTime)) {
                break Label_0065;
            }
            return false;
        }
        final Object this$respTime = this.getRespTime();
        final Object other$respTime = other.getRespTime();
        if (this$respTime == null) {
            if (other$respTime == null) {
                return true;
            }
        }
        else if (this$respTime.equals(other$respTime)) {
            return true;
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof RespRectifyTime;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $reqTime = this.getReqTime();
        result = result * 59 + (($reqTime == null) ? 43 : $reqTime.hashCode());
        final Object $respTime = this.getRespTime();
        result = result * 59 + (($respTime == null) ? 43 : $respTime.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "RespRectifyTime(reqTime=" + this.getReqTime() + ", respTime=" + this.getRespTime() + ")";
    }
}
