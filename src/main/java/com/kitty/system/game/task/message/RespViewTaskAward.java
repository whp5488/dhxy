package com.kitty.system.game.task.message;

import java.util.ArrayList;
import com.kitty.system.common.enter.FiedValue;
import java.util.List;
import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 61552)
public class RespViewTaskAward extends Message
{
    private int serial;
    private List<FiedValue> list;
    
    public RespViewTaskAward() {
        this.list = new ArrayList<>();
    }
    
    public int getSerial() {
        return this.serial;
    }
    
    public List<FiedValue> getList() {
        return this.list;
    }
    
    public void setSerial(final int serial) {
        this.serial = serial;
    }
    
    public void setList(final List<FiedValue> list) {
        this.list = list;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof RespViewTaskAward)) {
            return false;
        }
        RespViewTaskAward other = (RespViewTaskAward)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getSerial() != other.getSerial()) {
            return false;
        }
        Object this$list = this.getList();
        Object other$list = other.getList();
        return (this$list != null) ? this$list.equals(other$list) : (other$list == null);
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof RespViewTaskAward;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getSerial();
        Object $list = this.getList();
        result = result * 59 + (($list == null) ? 43 : $list.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "RespViewTaskAward(serial=" + this.getSerial() + ", list=" + this.getList() + ")";
    }
}
