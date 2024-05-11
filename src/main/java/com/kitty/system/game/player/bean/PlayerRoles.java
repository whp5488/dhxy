
package com.kitty.system.game.player.bean;

import java.util.HashSet;
import java.util.Set;
import java.io.Serializable;

public class PlayerRoles implements Serializable
{
    private Set<Long> list;
    
    public PlayerRoles() {
        this.list = new HashSet<Long>();
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
        final PlayerRoles other = (PlayerRoles)o;
        if (!other.canEqual(this)) {
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
        return other instanceof PlayerRoles;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $list = this.getList();
        result = result * 59 + (($list == null) ? 43 : $list.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "PlayerRoles(list=" + this.getList() + ")";
    }
}
