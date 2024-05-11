
package com.kitty.system.init.domain;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Table;
import java.io.Serializable;

@Table("t_data_monster_peripheral")
public class SysDataMonsterPeripheral implements Serializable
{
    @Column("serial")
    private int serial;
    @Column("weapon_id")
    private int weaponId;
    @Column("gem_num")
    private int gemNum;
    @Column("color")
    private byte color;
    
    public int getSerial() {
        return this.serial;
    }
    
    public int getWeaponId() {
        return this.weaponId;
    }
    
    public int getGemNum() {
        return this.gemNum;
    }
    
    public byte getColor() {
        return this.color;
    }
    
    public void setSerial(final int serial) {
        this.serial = serial;
    }
    
    public void setWeaponId(final int weaponId) {
        this.weaponId = weaponId;
    }
    
    public void setGemNum(final int gemNum) {
        this.gemNum = gemNum;
    }
    
    public void setColor(final byte color) {
        this.color = color;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SysDataMonsterPeripheral)) {
            return false;
        }
        final SysDataMonsterPeripheral other = (SysDataMonsterPeripheral)o;
        return other.canEqual(this) && this.getSerial() == other.getSerial() && this.getWeaponId() == other.getWeaponId() && this.getGemNum() == other.getGemNum() && this.getColor() == other.getColor();
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof SysDataMonsterPeripheral;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getSerial();
        result = result * 59 + this.getWeaponId();
        result = result * 59 + this.getGemNum();
        result = result * 59 + this.getColor();
        return result;
    }
    
    @Override
    public String toString() {
        return "SysDataMonsterPeripheral(serial=" + this.getSerial() + ", weaponId=" + this.getWeaponId() + ", gemNum=" + this.getGemNum() + ", color=" + this.getColor() + ")";
    }
}
