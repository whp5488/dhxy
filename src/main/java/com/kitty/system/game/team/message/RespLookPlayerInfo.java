package com.kitty.system.game.team.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 49152)
public class RespLookPlayerInfo extends Message
{
    private int leaderRoleId;
    private int roleId;
    private String name;
    private String faction;
    private int serial;
    private int teamMembersCount;
    private byte rei;
    private short level;
    private byte isPK;
    
    public RespLookPlayerInfo() {
        this.leaderRoleId = 0;
        this.teamMembersCount = 1;
        this.rei = 1;
        this.level = 1;
        this.isPK = 0;
    }
    
    public int getLeaderRoleId() {
        return this.leaderRoleId;
    }
    
    public int getRoleId() {
        return this.roleId;
    }
    
    public String getName() {
        return this.name;
    }
    
    public String getFaction() {
        return this.faction;
    }
    
    public int getSerial() {
        return this.serial;
    }
    
    public int getTeamMembersCount() {
        return this.teamMembersCount;
    }
    
    public byte getRei() {
        return this.rei;
    }
    
    public short getLevel() {
        return this.level;
    }
    
    public byte getIsPK() {
        return this.isPK;
    }
    
    public void setLeaderRoleId(final int leaderRoleId) {
        this.leaderRoleId = leaderRoleId;
    }
    
    public void setRoleId(final int roleId) {
        this.roleId = roleId;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public void setFaction(final String faction) {
        this.faction = faction;
    }
    
    public void setSerial(final int serial) {
        this.serial = serial;
    }
    
    public void setTeamMembersCount(final int teamMembersCount) {
        this.teamMembersCount = teamMembersCount;
    }
    
    public void setRei(final byte rei) {
        this.rei = rei;
    }
    
    public void setLevel(final short level) {
        this.level = level;
    }
    
    public void setIsPK(final byte isPK) {
        this.isPK = isPK;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof RespLookPlayerInfo)) {
            return false;
        }
        RespLookPlayerInfo other = (RespLookPlayerInfo)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getLeaderRoleId() != other.getLeaderRoleId()) {
            return false;
        }
        if (this.getRoleId() != other.getRoleId()) {
            return false;
        }
        Object this$name = this.getName();
        Object other$name = other.getName();
        if (!((this$name != null) ? this$name.equals(other$name) : (other$name == null))) {
            return false;
        }
        Object this$faction = this.getFaction();
        Object other$faction = other.getFaction();
        return ((this$faction != null) ? this$faction.equals(other$faction) : (other$faction == null)) && this.getSerial() == other.getSerial() && this.getTeamMembersCount() == other.getTeamMembersCount() && this.getRei() == other.getRei() && this.getLevel() == other.getLevel() && this.getIsPK() == other.getIsPK();
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof RespLookPlayerInfo;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getLeaderRoleId();
        result = result * 59 + this.getRoleId();
        Object $name = this.getName();
        result = result * 59 + (($name == null) ? 43 : $name.hashCode());
        Object $faction = this.getFaction();
        result = result * 59 + (($faction == null) ? 43 : $faction.hashCode());
        result = result * 59 + this.getSerial();
        result = result * 59 + this.getTeamMembersCount();
        result = result * 59 + this.getRei();
        result = result * 59 + this.getLevel();
        result = result * 59 + this.getIsPK();
        return result;
    }
    
    @Override
    public String toString() {
        return "RespLookPlayerInfo(leaderRoleId=" + this.getLeaderRoleId() + ", roleId=" + this.getRoleId() + ", name=" + this.getName() + ", faction=" + this.getFaction() + ", serial=" + this.getSerial() + ", teamMembersCount=" + this.getTeamMembersCount() + ", rei=" + (int)this.getRei() + ", level=" + (int)this.getLevel() + ", isPK=" + (int)this.getIsPK() + ")";
    }
}
