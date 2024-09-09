package com.kitty.system.game.activity.model.user.ca.vo;

import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;

public class CaFight implements Serializable
{
    private int teamId;
    private List<Long> members;
    private byte state;
    private byte session;
    
    public int getTeamId() {
        return this.teamId;
    }
    
    public List<Long> getMembers() {
        return this.members;
    }
    
    public byte getState() {
        return this.state;
    }
    
    public byte getSession() {
        return this.session;
    }
    
    public void setTeamId(final int teamId) {
        this.teamId = teamId;
    }
    
    public void setMembers(final List<Long> members) {
        this.members = members;
    }
    
    public void setState(final byte state) {
        this.state = state;
    }
    
    public void setSession(final byte session) {
        this.session = session;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof CaFight)) {
            return false;
        }
        CaFight other = (CaFight)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getTeamId() != other.getTeamId()) {
            return false;
        }
        Object this$members = this.getMembers();
        Object other$members = other.getMembers();
        return ((this$members != null) ? this$members.equals(other$members) : (other$members == null)) && this.getState() == other.getState() && this.getSession() == other.getSession();
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof CaFight;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getTeamId();
        Object $members = this.getMembers();
        result = result * 59 + (($members == null) ? 43 : $members.hashCode());
        result = result * 59 + this.getState();
        result = result * 59 + this.getSession();
        return result;
    }
    
    @Override
    public String toString() {
        return "CaFight(teamId=" + this.getTeamId() + ", members=" + this.getMembers() + ", state=" + (int)this.getState() + ", session=" + (int)this.getSession() + ")";
    }
    
    public CaFight(final int teamId, final List<Long> members, final byte state, final byte session) {
        this.members = new ArrayList<>();
        this.state = 0;
        this.session = 0;
        this.teamId = teamId;
        this.members = members;
        this.state = state;
        this.session = session;
    }
    
    public CaFight() {
        this.members = new ArrayList<>();
        this.state = 0;
        this.session = 0;
    }
}
