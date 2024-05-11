
package com.kitty.system.game.role.domain;

import com.kitty.system.game.player.PlayerProfile;

public class RoleInfo
{
    private String sole;
    private int id;
    private String name;
    private byte sex;
    private short level;
    private byte relive;
    private byte race;
    private short icon;
    private int serial;
    private byte drill;
    private String marking;
    
    public RoleInfo(final PlayerProfile profile) {
        this.sole = String.valueOf(profile.getId());
        this.id = profile.getRoleId();
        this.name = profile.getName();
        this.sex = profile.getSex();
        this.level = (short)profile.getLevel();
        this.relive = (byte)profile.getRelive();
        this.race = profile.getRace();
        this.icon = (short)profile.getIcon();
        this.serial = profile.getSerial();
        this.drill = profile.getDrill();
        this.drill = profile.getDrill();
        this.marking = profile.getMarking();
    }
    
    public String getSole() {
        return this.sole;
    }
    
    public int getId() {
        return this.id;
    }
    
    public String getName() {
        return this.name;
    }
    
    public byte getSex() {
        return this.sex;
    }
    
    public short getLevel() {
        return this.level;
    }
    
    public byte getRelive() {
        return this.relive;
    }
    
    public byte getRace() {
        return this.race;
    }
    
    public short getIcon() {
        return this.icon;
    }
    
    public int getSerial() {
        return this.serial;
    }
    
    public byte getDrill() {
        return this.drill;
    }
    
    public String getMarking() {
        return this.marking;
    }
    
    public void setSole(final String sole) {
        this.sole = sole;
    }
    
    public void setId(final int id) {
        this.id = id;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public void setSex(final byte sex) {
        this.sex = sex;
    }
    
    public void setLevel(final short level) {
        this.level = level;
    }
    
    public void setRelive(final byte relive) {
        this.relive = relive;
    }
    
    public void setRace(final byte race) {
        this.race = race;
    }
    
    public void setIcon(final short icon) {
        this.icon = icon;
    }
    
    public void setSerial(final int serial) {
        this.serial = serial;
    }
    
    public void setDrill(final byte drill) {
        this.drill = drill;
    }
    
    public void setMarking(final String marking) {
        this.marking = marking;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof RoleInfo)) {
            return false;
        }
        final RoleInfo other = (RoleInfo)o;
        if (!other.canEqual(this)) {
            return false;
        }
        final Object this$sole = this.getSole();
        final Object other$sole = other.getSole();
        Label_0065: {
            if (this$sole == null) {
                if (other$sole == null) {
                    break Label_0065;
                }
            }
            else if (this$sole.equals(other$sole)) {
                break Label_0065;
            }
            return false;
        }
        if (this.getId() != other.getId()) {
            return false;
        }
        final Object this$name = this.getName();
        final Object other$name = other.getName();
        Label_0115: {
            if (this$name == null) {
                if (other$name == null) {
                    break Label_0115;
                }
            }
            else if (this$name.equals(other$name)) {
                break Label_0115;
            }
            return false;
        }
        if (this.getSex() != other.getSex()) {
            return false;
        }
        if (this.getLevel() != other.getLevel()) {
            return false;
        }
        if (this.getRelive() != other.getRelive()) {
            return false;
        }
        if (this.getRace() != other.getRace()) {
            return false;
        }
        if (this.getIcon() != other.getIcon()) {
            return false;
        }
        if (this.getSerial() != other.getSerial()) {
            return false;
        }
        if (this.getDrill() != other.getDrill()) {
            return false;
        }
        final Object this$marking = this.getMarking();
        final Object other$marking = other.getMarking();
        if (this$marking == null) {
            if (other$marking == null) {
                return true;
            }
        }
        else if (this$marking.equals(other$marking)) {
            return true;
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof RoleInfo;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $sole = this.getSole();
        result = result * 59 + (($sole == null) ? 43 : $sole.hashCode());
        result = result * 59 + this.getId();
        final Object $name = this.getName();
        result = result * 59 + (($name == null) ? 43 : $name.hashCode());
        result = result * 59 + this.getSex();
        result = result * 59 + this.getLevel();
        result = result * 59 + this.getRelive();
        result = result * 59 + this.getRace();
        result = result * 59 + this.getIcon();
        result = result * 59 + this.getSerial();
        result = result * 59 + this.getDrill();
        final Object $marking = this.getMarking();
        result = result * 59 + (($marking == null) ? 43 : $marking.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "RoleInfo(sole=" + this.getSole() + ", id=" + this.getId() + ", name=" + this.getName() + ", sex=" + this.getSex() + ", level=" + this.getLevel() + ", relive=" + this.getRelive() + ", race=" + this.getRace() + ", icon=" + this.getIcon() + ", serial=" + this.getSerial() + ", drill=" + this.getDrill() + ", marking=" + this.getMarking() + ")";
    }
}
