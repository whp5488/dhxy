
package com.kitty.system.game.team.message;

import com.kitty.system.common.enter.FiedValue;
import java.util.List;
import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 61545)
public class RespFriendAddRole extends Message
{
    private int type1;
    private String type;
    private String name;
    private int online;
    private int insider_level;
    private List<FiedValue> list;
    
    public RespFriendAddRole() {
        this.type1 = 1;
        this.type = "6";
        this.online = 1;
        this.insider_level = 0;
    }
    
    public int getType1() {
        return this.type1;
    }
    
    public String getType() {
        return this.type;
    }
    
    public String getName() {
        return this.name;
    }
    
    public int getOnline() {
        return this.online;
    }
    
    public int getInsider_level() {
        return this.insider_level;
    }
    
    public List<FiedValue> getList() {
        return this.list;
    }
    
    public void setType1(final int type1) {
        this.type1 = type1;
    }
    
    public void setType(final String type) {
        this.type = type;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public void setOnline(final int online) {
        this.online = online;
    }
    
    public void setInsider_level(final int insider_level) {
        this.insider_level = insider_level;
    }
    
    public void setList(final List<FiedValue> list) {
        this.list = list;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof RespFriendAddRole)) {
            return false;
        }
        final RespFriendAddRole other = (RespFriendAddRole)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getType1() != other.getType1()) {
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
        if (this.getOnline() != other.getOnline()) {
            return false;
        }
        if (this.getInsider_level() != other.getInsider_level()) {
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
        return other instanceof RespFriendAddRole;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getType1();
        final Object $type = this.getType();
        result = result * 59 + (($type == null) ? 43 : $type.hashCode());
        final Object $name = this.getName();
        result = result * 59 + (($name == null) ? 43 : $name.hashCode());
        result = result * 59 + this.getOnline();
        result = result * 59 + this.getInsider_level();
        final Object $list = this.getList();
        result = result * 59 + (($list == null) ? 43 : $list.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "RespFriendAddRole(type1=" + this.getType1() + ", type=" + this.getType() + ", name=" + this.getName() + ", online=" + this.getOnline() + ", insider_level=" + this.getInsider_level() + ", list=" + this.getList() + ")";
    }
}
