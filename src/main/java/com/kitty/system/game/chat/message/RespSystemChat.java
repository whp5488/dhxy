package com.kitty.system.game.chat.message;

import java.util.ArrayList;
import com.kitty.system.common.enter.FiedValue;
import java.util.List;
import com.kitty.mina.annotation.StringField;
import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 16384)
public class RespSystemChat extends Message
{
    private int type;
    private int serial;
    private String name;
    @StringField(1)
    private String msg;
    private int time;
    private List<FiedValue> list;
    
    public RespSystemChat() {
        this.serial = 0;
        this.name = "系统";
        this.time = new Long(System.currentTimeMillis() / 1000L).intValue();
        this.list = new ArrayList<>();
    }
    
    public int getType() {
        return this.type;
    }
    
    public int getSerial() {
        return this.serial;
    }
    
    public String getName() {
        return this.name;
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
    
    public void setType(final int type) {
        this.type = type;
    }
    
    public void setSerial(final int serial) {
        this.serial = serial;
    }
    
    public void setName(final String name) {
        this.name = name;
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
        if (!(o instanceof RespSystemChat)) {
            return false;
        }
        RespSystemChat other = (RespSystemChat)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getType() != other.getType()) {
            return false;
        }
        if (this.getSerial() != other.getSerial()) {
            return false;
        }
        Object this$name = this.getName();
        Object other$name = other.getName();
        if (!((this$name != null) ? this$name.equals(other$name) : (other$name == null))) {
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
        return other instanceof RespSystemChat;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getType();
        result = result * 59 + this.getSerial();
        Object $name = this.getName();
        result = result * 59 + (($name == null) ? 43 : $name.hashCode());
        Object $msg = this.getMsg();
        result = result * 59 + (($msg == null) ? 43 : $msg.hashCode());
        result = result * 59 + this.getTime();
        Object $list = this.getList();
        result = result * 59 + (($list == null) ? 43 : $list.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "RespSystemChat(type=" + this.getType() + ", serial=" + this.getSerial() + ", name=" + this.getName() + ", msg=" + this.getMsg() + ", time=" + this.getTime() + ", list=" + this.getList() + ")";
    }
}
