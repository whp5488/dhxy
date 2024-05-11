
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
        final RespFriendUpdatePartial other = (RespFriendUpdatePartial)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getUpdate_type() != other.getUpdate_type()) {
            return false;
        }
        final Object this$type = this.getType();
        final Object other$type = other.getType();
        Label_0078: {
            if (this$type == null) {
                if (other$type == null) {
                    break Label_0078;
                }
            }
            else if (this$type.equals(other$type)) {
                break Label_0078;
            }
            return false;
        }
        final Object this$name = this.getName();
        final Object other$name = other.getName();
        Label_0115: {
            if (this$name == null) {
                if (other$name == null) {
                    break Label_0115;
                }
            }
            else if (this$name.equals(other$name)) {
                break Label_0115;
            }
            return false;
        }
        final Object this$list = this.getList();
        final Object other$list = other.getList();
        if (this$list == null) {
            if (other$list == null) {
                return true;
            }
        }
        else if (this$list.equals(other$list)) {
            return true;
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof RespFriendUpdatePartial;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getUpdate_type();
        final Object $type = this.getType();
        result = result * 59 + (($type == null) ? 43 : $type.hashCode());
        final Object $name = this.getName();
        result = result * 59 + (($name == null) ? 43 : $name.hashCode());
        final Object $list = this.getList();
        result = result * 59 + (($list == null) ? 43 : $list.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "RespFriendUpdatePartial(update_type=" + this.getUpdate_type() + ", type=" + this.getType() + ", name=" + this.getName() + ", list=" + this.getList() + ")";
    }
}
