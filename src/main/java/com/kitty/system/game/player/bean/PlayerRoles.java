package com.kitty.system.game.player.bean;

import java.util.HashSet;
import java.util.Set;
import java.io.Serializable;

public class PlayerRoles implements Serializable
{
    private Set<Long> list;
    
    public PlayerRoles() {
        this.list = new HashSet();
    }
    
    public Set<Long> getList() {
        return this.list;
    }
    
    public void setList(final Set<Long> list) {
        this.list = list;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof PlayerRoles)) {
            return false;
        }
        PlayerRoles other = (PlayerRoles)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$list = this.getList();
        Object other$list = other.getList();
        return (this$list != null) ? this$list.equals(other$list) : (other$list == null);
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof PlayerRoles;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Object $list = this.getList();
        result = result * 59 + (($list == null) ? 43 : $list.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "PlayerRoles(list=" + this.getList() + ")";
    }
}
