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
        RespFriendAddRole other = (RespFriendAddRole)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getType1() != other.getType1()) {
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
        if (this.getOnline() != other.getOnline()) {
            return false;
        }
        if (this.getInsider_level() != other.getInsider_level()) {
            return false;
        }
        Object this$list = this.getList();
        Object other$list = other.getList();
        return (this$list != null) ? this$list.equals(other$list) : (other$list == null);
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof RespFriendAddRole;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getType1();
        Object $type = this.getType();
        result = result * 59 + (($type == null) ? 43 : $type.hashCode());
        Object $name = this.getName();
        result = result * 59 + (($name == null) ? 43 : $name.hashCode());
        result = result * 59 + this.getOnline();
        result = result * 59 + this.getInsider_level();
        Object $list = this.getList();
        result = result * 59 + (($list == null) ? 43 : $list.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "RespFriendAddRole(type1=" + this.getType1() + ", type=" + this.getType() + ", name=" + this.getName() + ", online=" + this.getOnline() + ", insider_level=" + this.getInsider_level() + ", list=" + this.getList() + ")";
    }
}
