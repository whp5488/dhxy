package com.kitty.system.game.friend.message.vo;

public class Pla
{
    private int roleId;
    private String name;
    private byte rei;
    private short level;
    private int serial;
    private byte status;
    private String close;
    private String groupId;
    
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
    
    public int getSerial() {
        return this.serial;
    }
    
    public byte getStatus() {
        return this.status;
    }
    
    public String getClose() {
        return this.close;
    }
    
    public String getGroupId() {
        return this.groupId;
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
    
    public void setSerial(final int serial) {
        this.serial = serial;
    }
    
    public void setStatus(final byte status) {
        this.status = status;
    }
    
    public void setClose(final String close) {
        this.close = close;
    }
    
    public void setGroupId(final String groupId) {
        this.groupId = groupId;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Pla)) {
            return false;
        }
        Pla other = (Pla)o;
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
        if (this.getSerial() != other.getSerial()) {
            return false;
        }
        if (this.getStatus() != other.getStatus()) {
            return false;
        }
        Object this$close = this.getClose();
        Object other$close = other.getClose();
        if (!((this$close != null) ? this$close.equals(other$close) : (other$close == null))) {
            return false;
        }
        Object this$groupId = this.getGroupId();
        Object other$groupId = other.getGroupId();
        return (this$groupId != null) ? this$groupId.equals(other$groupId) : (other$groupId == null);
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof Pla;
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
        result = result * 59 + this.getSerial();
        result = result * 59 + this.getStatus();
        Object $close = this.getClose();
        result = result * 59 + (($close == null) ? 43 : $close.hashCode());
        Object $groupId = this.getGroupId();
        result = result * 59 + (($groupId == null) ? 43 : $groupId.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "Pla(roleId=" + this.getRoleId() + ", name=" + this.getName() + ", rei=" + (int)this.getRei() + ", level=" + (int)this.getLevel() + ", serial=" + this.getSerial() + ", status=" + (int)this.getStatus() + ", close=" + this.getClose() + ", groupId=" + this.getGroupId() + ")";
    }
    
    public Pla(final int roleId, final String name, final byte rei, final short level, final int serial, final byte status, final String close, final String groupId) {
        this.roleId = roleId;
        this.name = name;
        this.rei = rei;
        this.level = level;
        this.serial = serial;
        this.status = status;
        this.close = close;
        this.groupId = groupId;
    }
    
    public Pla() {
    }
}
