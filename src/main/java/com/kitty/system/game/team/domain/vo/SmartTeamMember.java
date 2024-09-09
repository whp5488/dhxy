package com.kitty.system.game.team.domain.vo;

import com.kitty.system.game.team.domain.Member;
import java.io.Serializable;

public class SmartTeamMember implements Serializable
{
    private int roleId;
    private String name;
    private short level;
    private byte rebirth;
    private int serial;
    
    public SmartTeamMember() {
    }
    
    public SmartTeamMember(Member member) {
        this.roleId = (int)member.getRoleId();
        this.name = member.getName();
        this.level = member.getLevel();
        this.rebirth = member.getRelive();
        this.serial = (int)member.getSerial();
    }
    
    public int getRoleId() {
        return this.roleId;
    }
    
    public String getName() {
        return this.name;
    }
    
    public short getLevel() {
        return this.level;
    }
    
    public byte getRebirth() {
        return this.rebirth;
    }
    
    public int getSerial() {
        return this.serial;
    }
    
    public void setRoleId(final int roleId) {
        this.roleId = roleId;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public void setLevel(final short level) {
        this.level = level;
    }
    
    public void setRebirth(final byte rebirth) {
        this.rebirth = rebirth;
    }
    
    public void setSerial(final int serial) {
        this.serial = serial;
    }
}
