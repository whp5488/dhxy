package com.kitty.system.game.team.domain.vo;

import java.util.LinkedHashMap;
import java.io.Serializable;

public class TeamRequest implements Serializable
{
    private LinkedHashMap<Integer, String> map;
    
    public TeamRequest() {
    }
    
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
        TeamRequest other = (TeamRequest)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$map = this.getMap();
        Object other$map = other.getMap();
        return (this$map != null) ? this$map.equals(other$map) : (other$map == null);
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof TeamRequest;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Object $map = this.getMap();
        result = result * 59 + (($map == null) ? 43 : $map.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "TeamRequest(map=" + this.getMap() + ")";
    }
}
