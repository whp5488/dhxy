package com.kitty.system.game.pk.message;

import java.util.ArrayList;
import com.kitty.system.common.enter.FiedValue;
import java.util.List;
import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 45228)
public class RespTraceRole extends Message
{
    private int roleId;
    private int serial;
    private String name;
    private byte relive;
    private short level;
    private int weapon;
    private int gem;
    private List<Integer> lightEffect;
    private List<Integer> specialIcon;
    private List<FiedValue> list;
    private short map;
    private short x;
    private short y;
    
    public RespTraceRole() {
        this.relive = 0;
        this.level = 0;
        this.weapon = 0;
        this.gem = 0;
        this.lightEffect = new ArrayList<>();
        this.specialIcon = new ArrayList<>();
        this.map = 0;
        this.x = 0;
        this.y = 0;
    }
    
    public int getRoleId() {
        return this.roleId;
    }
    
    public int getSerial() {
        return this.serial;
    }
    
    public String getName() {
        return this.name;
    }
    
    public byte getRelive() {
        return this.relive;
    }
    
    public short getLevel() {
        return this.level;
    }
    
    public int getWeapon() {
        return this.weapon;
    }
    
    public int getGem() {
        return this.gem;
    }
    
    public List<Integer> getLightEffect() {
        return this.lightEffect;
    }
    
    public List<Integer> getSpecialIcon() {
        return this.specialIcon;
    }
    
    public List<FiedValue> getList() {
        return this.list;
    }
    
    public short getMap() {
        return this.map;
    }
    
    public short getX() {
        return this.x;
    }
    
    public short getY() {
        return this.y;
    }
    
    public void setRoleId(final int roleId) {
        this.roleId = roleId;
    }
    
    public void setSerial(final int serial) {
        this.serial = serial;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public void setRelive(final byte relive) {
        this.relive = relive;
    }
    
    public void setLevel(final short level) {
        this.level = level;
    }
    
    public void setWeapon(final int weapon) {
        this.weapon = weapon;
    }
    
    public void setGem(final int gem) {
        this.gem = gem;
    }
    
    public void setLightEffect(final List<Integer> lightEffect) {
        this.lightEffect = lightEffect;
    }
    
    public void setSpecialIcon(final List<Integer> specialIcon) {
        this.specialIcon = specialIcon;
    }
    
    public void setList(final List<FiedValue> list) {
        this.list = list;
    }
    
    public void setMap(final short map) {
        this.map = map;
    }
    
    public void setX(final short x) {
        this.x = x;
    }
    
    public void setY(final short y) {
        this.y = y;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof RespTraceRole)) {
            return false;
        }
        RespTraceRole other = (RespTraceRole)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getRoleId() != other.getRoleId()) {
            return false;
        }
        if (this.getSerial() != other.getSerial()) {
            return false;
        }
        Object this$name = this.getName();
        Object other$name = other.getName();
        if (!((this$name != null) ? this$name.equals(other$name) : (other$name == null))) {
            return false;
        }
        if (this.getRelive() != other.getRelive()) {
            return false;
        }
        if (this.getLevel() != other.getLevel()) {
            return false;
        }
        if (this.getWeapon() != other.getWeapon()) {
            return false;
        }
        if (this.getGem() != other.getGem()) {
            return false;
        }
        Object this$lightEffect = this.getLightEffect();
        Object other$lightEffect = other.getLightEffect();
        if (!((this$lightEffect != null) ? this$lightEffect.equals(other$lightEffect) : (other$lightEffect == null))) {
            return false;
        }
        Object this$specialIcon = this.getSpecialIcon();
        Object other$specialIcon = other.getSpecialIcon();
        if (!((this$specialIcon != null) ? this$specialIcon.equals(other$specialIcon) : (other$specialIcon == null))) {
            return false;
        }
        Object this$list = this.getList();
        Object other$list = other.getList();
        return ((this$list != null) ? this$list.equals(other$list) : (other$list == null)) && this.getMap() == other.getMap() && this.getX() == other.getX() && this.getY() == other.getY();
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof RespTraceRole;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getRoleId();
        result = result * 59 + this.getSerial();
        Object $name = this.getName();
        result = result * 59 + (($name == null) ? 43 : $name.hashCode());
        result = result * 59 + this.getRelive();
        result = result * 59 + this.getLevel();
        result = result * 59 + this.getWeapon();
        result = result * 59 + this.getGem();
        Object $lightEffect = this.getLightEffect();
        result = result * 59 + (($lightEffect == null) ? 43 : $lightEffect.hashCode());
        Object $specialIcon = this.getSpecialIcon();
        result = result * 59 + (($specialIcon == null) ? 43 : $specialIcon.hashCode());
        Object $list = this.getList();
        result = result * 59 + (($list == null) ? 43 : $list.hashCode());
        result = result * 59 + this.getMap();
        result = result * 59 + this.getX();
        result = result * 59 + this.getY();
        return result;
    }
    
    @Override
    public String toString() {
        return "RespTraceRole(roleId=" + this.getRoleId() + ", serial=" + this.getSerial() + ", name=" + this.getName() + ", relive=" + (int)this.getRelive() + ", level=" + (int)this.getLevel() + ", weapon=" + this.getWeapon() + ", gem=" + this.getGem() + ", lightEffect=" + this.getLightEffect() + ", specialIcon=" + this.getSpecialIcon() + ", list=" + this.getList() + ", map=" + (int)this.getMap() + ", x=" + (int)this.getX() + ", y=" + (int)this.getY() + ")";
    }
}
