package com.kitty.system.game.team.message;

import com.kitty.system.common.enter.FiedValue;
import java.util.List;
import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 24505)
public class RespFriendUpdatePartial extends Message
{
    private int update_type;
    private String type;
    private String name;
    private List<FiedValue> list;
    
    public RespFriendUpdatePartial() {
        this.update_type = 1;
        this.type = "6";
    }
    
    public int getUpdate_type() {
        return this.update_type;
    }
    
    public String getType() {
        return this.type;
    }
    
    public String getName() {
        return this.name;
    }
    
    public List<FiedValue> getList() {
        return this.list;
    }
    
    public void setUpdate_type(final int update_type) {
        this.update_type = update_type;
    }
    
    public void setType(final String type) {
        this.type = type;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public void setList(final List<FiedValue> list) {
        this.list = list;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof RespFriendUpdatePartial)) {
            return false;
        }
        RespFriendUpdatePartial other = (RespFriendUpdatePartial)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getUpdate_type() != other.getUpdate_type()) {
            return false;
        }
        Object this$type = this.getType();
        Object other$type = other.getType();
        if (!((this$type != null) ? this$type.equals(other$type) : (other$type == null))) {
            return false;
        }
        Object this$name = this.getName();
        Object other$name = other.getName();
        if (!((this$name != null) ? this$name.equals(other$name) : (other$name == null))) {
            return false;
        }
        Object this$list = this.getList();
        Object other$list = other.getList();
        return (this$list != null) ? this$list.equals(other$list) : (other$list == null);
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof RespFriendUpdatePartial;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getUpdate_type();
        Object $type = this.getType();
        result = result * 59 + (($type == null) ? 43 : $type.hashCode());
        Object $name = this.getName();
        result = result * 59 + (($name == null) ? 43 : $name.hashCode());
        Object $list = this.getList();
        result = result * 59 + (($list == null) ? 43 : $list.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "RespFriendUpdatePartial(update_type=" + this.getUpdate_type() + ", type=" + this.getType() + ", name=" + this.getName() + ", list=" + this.getList() + ")";
    }
}
