package com.kitty.system.game.friend.message;

import java.util.ArrayList;
import java.util.List;
import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 45199)
public class ReqAlterFriendGroup extends Message
{
    private String id;
    private String name;
    private List<Integer> list;
    
    public ReqAlterFriendGroup() {
        this.list = new ArrayList<>();
    }
    
    public String getId() {
        return this.id;
    }
    
    public String getName() {
        return this.name;
    }
    
    public List<Integer> getList() {
        return this.list;
    }
    
    public void setId(final String id) {
        this.id = id;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public void setList(final List<Integer> list) {
        this.list = list;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ReqAlterFriendGroup)) {
            return false;
        }
        ReqAlterFriendGroup other = (ReqAlterFriendGroup)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$id = this.getId();
        Object other$id = other.getId();
        if (!((this$id != null) ? this$id.equals(other$id) : (other$id == null))) {
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
        return other instanceof ReqAlterFriendGroup;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Object $id = this.getId();
        result = result * 59 + (($id == null) ? 43 : $id.hashCode());
        Object $name = this.getName();
        result = result * 59 + (($name == null) ? 43 : $name.hashCode());
        Object $list = this.getList();
        result = result * 59 + (($list == null) ? 43 : $list.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "ReqAlterFriendGroup(id=" + this.getId() + ", name=" + this.getName() + ", list=" + this.getList() + ")";
    }
}
