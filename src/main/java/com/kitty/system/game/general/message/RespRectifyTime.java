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
    
    public RespRectifyTime(String reqTime) {
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
        RespRectifyTime other = (RespRectifyTime)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$reqTime = this.getReqTime();
        Object other$reqTime = other.getReqTime();
        if (!((this$reqTime != null) ? this$reqTime.equals(other$reqTime) : (other$reqTime == null))) {
            return false;
        }
        Object this$respTime = this.getRespTime();
        Object other$respTime = other.getRespTime();
        return (this$respTime != null) ? this$respTime.equals(other$respTime) : (other$respTime == null);
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof RespRectifyTime;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Object $reqTime = this.getReqTime();
        result = result * 59 + (($reqTime == null) ? 43 : $reqTime.hashCode());
        Object $respTime = this.getRespTime();
        result = result * 59 + (($respTime == null) ? 43 : $respTime.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "RespRectifyTime(reqTime=" + this.getReqTime() + ", respTime=" + this.getRespTime() + ")";
    }
}
