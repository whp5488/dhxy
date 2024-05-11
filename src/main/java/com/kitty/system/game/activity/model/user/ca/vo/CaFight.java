
package com.kitty.system.game.activity.model.user.ca.vo;

import java.beans.ConstructorProperties;
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
        final CaFight other = (CaFight)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getTeamId() != other.getTeamId()) {
            return false;
        }
        final Object this$members = this.getMembers();
        final Object other$members = other.getMembers();
        if (this$members == null) {
            if (other$members == null) {
                return this.getState() == other.getState() && this.getSession() == other.getSession();
            }
        }
        else if (this$members.equals(other$members)) {
            return this.getState() == other.getState() && this.getSession() == other.getSession();
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof CaFight;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getTeamId();
        final Object $members = this.getMembers();
        result = result * 59 + (($members == null) ? 43 : $members.hashCode());
        result = result * 59 + this.getState();
        result = result * 59 + this.getSession();
        return result;
    }
    
    @Override
    public String toString() {
        return "CaFight(teamId=" + this.getTeamId() + ", members=" + this.getMembers() + ", state=" + this.getState() + ", session=" + this.getSession() + ")";
    }
    
    @ConstructorProperties({ "teamId", "members", "state", "session" })
    public CaFight(final int teamId, final List<Long> members, final byte state, final byte session) {
        this.members = new ArrayList<Long>();
        this.state = 0;
        this.session = 0;
        this.teamId = teamId;
        this.members = members;
        this.state = state;
        this.session = session;
    }
    
    public CaFight() {
        this.members = new ArrayList<Long>();
        this.state = 0;
        this.session = 0;
    }
}
