package com.kitty.system.game.enter.message;

import com.kitty.mina.annotation.StringField;
import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 20480)
public class RespNotifyMisc extends Message
{
    @StringField(1)
    private String msg;
    private int time;
    private int friendId;
    
    public RespNotifyMisc(String msg) {
        this.msg = msg;
        this.time = new Long(System.currentTimeMillis() / 1000L).intValue();
    }
    
    public RespNotifyMisc() {
    }
    
    public String getMsg() {
        return this.msg;
    }
    
    public int getTime() {
        return this.time;
    }
    
    public int getFriendId() {
        return this.friendId;
    }
    
    public void setMsg(final String msg) {
        this.msg = msg;
    }
    
    public void setTime(final int time) {
        this.time = time;
    }
    
    public void setFriendId(final int friendId) {
        this.friendId = friendId;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof RespNotifyMisc)) {
            return false;
        }
        RespNotifyMisc other = (RespNotifyMisc)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$msg = this.getMsg();
        Object other$msg = other.getMsg();
        return ((this$msg != null) ? this$msg.equals(other$msg) : (other$msg == null)) && this.getTime() == other.getTime() && this.getFriendId() == other.getFriendId();
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof RespNotifyMisc;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Object $msg = this.getMsg();
        result = result * 59 + (($msg == null) ? 43 : $msg.hashCode());
        result = result * 59 + this.getTime();
        result = result * 59 + this.getFriendId();
        return result;
    }
    
    @Override
    public String toString() {
        return "RespNotifyMisc(msg=" + this.getMsg() + ", time=" + this.getTime() + ", friendId=" + this.getFriendId() + ")";
    }
}
