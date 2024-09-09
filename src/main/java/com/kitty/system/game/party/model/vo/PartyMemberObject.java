package com.kitty.system.game.party.model.vo;

public class PartyMemberObject
{
    private int roleId;
    private String name;
    private byte rei;
    private short level;
    private int roleSerial;
    private byte job;
    private int joinTime;
    private int weekBangBtl;
    private int thisBangBtl;
    private int weekEcType;
    private int partyTask;
    private String contribute;
    private int lastOnlineTime;
    private byte onLineStatus;
    
    public PartyMemberObject() {
        this.weekBangBtl = 0;
        this.thisBangBtl = 0;
        this.weekEcType = 0;
        this.partyTask = 0;
        this.contribute = "0";
        this.onLineStatus = 0;
    }
    
    public int getRoleId() {
        return this.roleId;
    }
    
    public String getName() {
        return this.name;
    }
    
    public byte getRei() {
        return this.rei;
    }
    
    public short getLevel() {
        return this.level;
    }
    
    public int getRoleSerial() {
        return this.roleSerial;
    }
    
    public byte getJob() {
        return this.job;
    }
    
    public int getJoinTime() {
        return this.joinTime;
    }
    
    public int getWeekBangBtl() {
        return this.weekBangBtl;
    }
    
    public int getThisBangBtl() {
        return this.thisBangBtl;
    }
    
    public int getWeekEcType() {
        return this.weekEcType;
    }
    
    public int getPartyTask() {
        return this.partyTask;
    }
    
    public String getContribute() {
        return this.contribute;
    }
    
    public int getLastOnlineTime() {
        return this.lastOnlineTime;
    }
    
    public byte getOnLineStatus() {
        return this.onLineStatus;
    }
    
    public void setRoleId(final int roleId) {
        this.roleId = roleId;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public void setRei(final byte rei) {
        this.rei = rei;
    }
    
    public void setLevel(final short level) {
        this.level = level;
    }
    
    public void setRoleSerial(final int roleSerial) {
        this.roleSerial = roleSerial;
    }
    
    public void setJob(final byte job) {
        this.job = job;
    }
    
    public void setJoinTime(final int joinTime) {
        this.joinTime = joinTime;
    }
    
    public void setWeekBangBtl(final int weekBangBtl) {
        this.weekBangBtl = weekBangBtl;
    }
    
    public void setThisBangBtl(final int thisBangBtl) {
        this.thisBangBtl = thisBangBtl;
    }
    
    public void setWeekEcType(final int weekEcType) {
        this.weekEcType = weekEcType;
    }
    
    public void setPartyTask(final int partyTask) {
        this.partyTask = partyTask;
    }
    
    public void setContribute(final String contribute) {
        this.contribute = contribute;
    }
    
    public void setLastOnlineTime(final int lastOnlineTime) {
        this.lastOnlineTime = lastOnlineTime;
    }
    
    public void setOnLineStatus(final byte onLineStatus) {
        this.onLineStatus = onLineStatus;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof PartyMemberObject)) {
            return false;
        }
        PartyMemberObject other = (PartyMemberObject)o;
        if (!other.canEqual(this)) {
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
        if (this.getRei() != other.getRei()) {
            return false;
        }
        if (this.getLevel() != other.getLevel()) {
            return false;
        }
        if (this.getRoleSerial() != other.getRoleSerial()) {
            return false;
        }
        if (this.getJob() != other.getJob()) {
            return false;
        }
        if (this.getJoinTime() != other.getJoinTime()) {
            return false;
        }
        if (this.getWeekBangBtl() != other.getWeekBangBtl()) {
            return false;
        }
        if (this.getThisBangBtl() != other.getThisBangBtl()) {
            return false;
        }
        if (this.getWeekEcType() != other.getWeekEcType()) {
            return false;
        }
        if (this.getPartyTask() != other.getPartyTask()) {
            return false;
        }
        Object this$contribute = this.getContribute();
        Object other$contribute = other.getContribute();
        return ((this$contribute != null) ? this$contribute.equals(other$contribute) : (other$contribute == null)) && this.getLastOnlineTime() == other.getLastOnlineTime() && this.getOnLineStatus() == other.getOnLineStatus();
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof PartyMemberObject;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getRoleId();
        Object $name = this.getName();
        result = result * 59 + (($name == null) ? 43 : $name.hashCode());
        result = result * 59 + this.getRei();
        result = result * 59 + this.getLevel();
        result = result * 59 + this.getRoleSerial();
        result = result * 59 + this.getJob();
        result = result * 59 + this.getJoinTime();
        result = result * 59 + this.getWeekBangBtl();
        result = result * 59 + this.getThisBangBtl();
        result = result * 59 + this.getWeekEcType();
        result = result * 59 + this.getPartyTask();
        Object $contribute = this.getContribute();
        result = result * 59 + (($contribute == null) ? 43 : $contribute.hashCode());
        result = result * 59 + this.getLastOnlineTime();
        result = result * 59 + this.getOnLineStatus();
        return result;
    }
    
    @Override
    public String toString() {
        return "PartyMemberObject(roleId=" + this.getRoleId() + ", name=" + this.getName() + ", rei=" + (int)this.getRei() + ", level=" + (int)this.getLevel() + ", roleSerial=" + this.getRoleSerial() + ", job=" + (int)this.getJob() + ", joinTime=" + this.getJoinTime() + ", weekBangBtl=" + this.getWeekBangBtl() + ", thisBangBtl=" + this.getThisBangBtl() + ", weekEcType=" + this.getWeekEcType() + ", partyTask=" + this.getPartyTask() + ", contribute=" + this.getContribute() + ", lastOnlineTime=" + this.getLastOnlineTime() + ", onLineStatus=" + (int)this.getOnLineStatus() + ")";
    }
}
