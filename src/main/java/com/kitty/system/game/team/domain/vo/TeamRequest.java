
package com.kitty.system.game.team.domain.vo;

import java.util.LinkedHashMap;
import java.io.Serializable;

public class TeamRequest implements Serializable
{
    private LinkedHashMap<Integer, String> map;
    
    public LinkedHashMap<Integer, String> getMap() {
        return this.map;
    }
    
    public void setMap(final LinkedHashMap<Integer, String> map) {
        this.map = map;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof TeamRequest)) {
            return false;
        }
        final TeamRequest other = (TeamRequest)o;
        if (!other.canEqual(this)) {
            return false;
        }
        final Object this$map = this.getMap();
        final Object other$map = other.getMap();
        if (this$map == null) {
            if (other$map == null) {
                return true;
            }
        }
        else if (this$map.equals(other$map)) {
            return true;
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof TeamRequest;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $map = this.getMap();
        result = result * 59 + (($map == null) ? 43 : $map.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "TeamRequest(map=" + this.getMap() + ")";
    }
}
