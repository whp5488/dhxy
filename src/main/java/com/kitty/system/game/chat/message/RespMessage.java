package com.kitty.system.game.chat.message;

import java.util.ArrayList;
import com.kitty.system.common.enter.FiedValue;
import java.util.List;
import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 12287)
public class RespMessage extends Message
{
    private int id;
    private int channel;
    private String msg;
    private int time;
    private List<FiedValue> list;
    
    public RespMessage() {
        this.time = new Long(System.currentTimeMillis() / 1000L).intValue();
        this.list = new ArrayList<>();
    }
    
    public int getId() {
        return this.id;
    }
    
    public int getChannel() {
        return this.channel;
    }
    
    public String getMsg() {
        return this.msg;
    }
    
    public int getTime() {
        return this.time;
    }
    
    public List<FiedValue> getList() {
        return this.list;
    }
    
    public void setId(final int id) {
        this.id = id;
    }
    
    public void setChannel(final int channel) {
        this.channel = channel;
    }
    
    public void setMsg(final String msg) {
        this.msg = msg;
    }
    
    public void setTime(final int time) {
        this.time = time;
    }
    
    public void setList(final List<FiedValue> list) {
        this.list = list;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof RespMessage)) {
            return false;
        }
        RespMessage other = (RespMessage)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getId() != other.getId()) {
            return false;
        }
        if (this.getChannel() != other.getChannel()) {
            return false;
        }
        Object this$msg = this.getMsg();
        Object other$msg = other.getMsg();
        if (!((this$msg != null) ? this$msg.equals(other$msg) : (other$msg == null))) {
            return false;
        }
        if (this.getTime() != other.getTime()) {
            return false;
        }
        Object this$list = this.getList();
        Object other$list = other.getList();
        return (this$list != null) ? this$list.equals(other$list) : (other$list == null);
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof RespMessage;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getId();
        result = result * 59 + this.getChannel();
        Object $msg = this.getMsg();
        result = result * 59 + (($msg == null) ? 43 : $msg.hashCode());
        result = result * 59 + this.getTime();
        Object $list = this.getList();
        result = result * 59 + (($list == null) ? 43 : $list.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "RespMessage(id=" + this.getId() + ", channel=" + this.getChannel() + ", msg=" + this.getMsg() + ", time=" + this.getTime() + ", list=" + this.getList() + ")";
    }
}
