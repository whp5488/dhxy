
package com.kitty.system.game.pk.message.vo;

import com.kitty.system.game.role.domain.Role;

public class PrisonerInfo
{
    private int roleId;
    private String name;
    private int serial;
    private byte rei;
    private short level;
    private int lastTi;
    
    public PrisonerInfo() {
    }
    
    public PrisonerInfo(final Role role) {
        this.roleId = role.getRoleId();
        this.name = role.getName();
        this.serial = role.getSerial();
        this.rei = role.getRelive().byteValue();
        this.level = role.getLevel().shortValue();
        this.lastTi = role.getPkInfoBox().getPrisonTime();
    }
    
    public PrisonerInfo(final int roleId, final String name, final int serial, final byte rei, final short level, final int lastTi) {
        this.roleId = roleId;
        this.name = name;
        this.serial = serial;
        this.rei = rei;
        this.level = level;
        this.lastTi = lastTi;
    }
    
    public int getRoleId() {
        return this.roleId;
    }
    
    public String getName() {
        return this.name;
    }
    
    public int getSerial() {
        return this.serial;
    }
    
    public byte getRei() {
        return this.rei;
    }
    
    public short getLevel() {
        return this.level;
    }
    
    public int getLastTi() {
        return this.lastTi;
    }
    
    public void setRoleId(final int roleId) {
        this.roleId = roleId;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public void setSerial(final int serial) {
        this.serial = serial;
    }
    
    public void setRei(final byte rei) {
        this.rei = rei;
    }
    
    public void setLevel(final short level) {
        this.level = level;
    }
    
    public void setLastTi(final int lastTi) {
        this.lastTi = lastTi;
    }
}
