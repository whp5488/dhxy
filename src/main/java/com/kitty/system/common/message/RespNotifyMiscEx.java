
package com.kitty.system.common.message;

import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.system.game.chat.service.ChatService;
import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 20481)
public class RespNotifyMiscEx extends Message
{
    private String msg;
    private int time;
    
    public RespNotifyMiscEx setMsg(final String msg) {
        this.msg = msg;
        return this;
    }
    
    public RespNotifyMiscEx(final Long id, final String msg, final boolean flag) {
        this.msg = msg;
        this.time = new Long(System.currentTimeMillis() / 1000L).intValue();
        if (flag) {
            SpringUtils.getBean(ChatService.class).sendInfo(id, msg);
        }
    }
    
    public RespNotifyMiscEx(final String msg) {
        this.msg = msg;
        this.time = new Long(System.currentTimeMillis() / 1000L).intValue();
    }
    
    public RespNotifyMiscEx() {
    }
    
    public String getMsg() {
        return this.msg;
    }
    
    public int getTime() {
        return this.time;
    }
    
    public void setTime(final int time) {
        this.time = time;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof RespNotifyMiscEx)) {
            return false;
        }
        final RespNotifyMiscEx other = (RespNotifyMiscEx)o;
        if (!other.canEqual(this)) {
            return false;
        }
        final Object this$msg = this.getMsg();
        final Object other$msg = other.getMsg();
        if (this$msg == null) {
            if (other$msg == null) {
                return this.getTime() == other.getTime();
            }
        }
        else if (this$msg.equals(other$msg)) {
            return this.getTime() == other.getTime();
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof RespNotifyMiscEx;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $msg = this.getMsg();
        result = result * 59 + (($msg == null) ? 43 : $msg.hashCode());
        result = result * 59 + this.getTime();
        return result;
    }
    
    @Override
    public String toString() {
        return "RespNotifyMiscEx(msg=" + this.getMsg() + ", time=" + this.getTime() + ")";
    }
}
