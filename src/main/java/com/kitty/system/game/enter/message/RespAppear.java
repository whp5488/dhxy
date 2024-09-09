package com.kitty.system.game.enter.message;

import java.util.ArrayList;
import com.kitty.system.common.enter.FiedValue;
import java.util.List;
import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 65529)
public class RespAppear extends Message
{
    private int roleId;
    private int posX;
    private int posY;
    private int serial;
    private String partyName;
    private byte job;
    private String name;
    private int relive;
    private int level;
    private int title;
    private int leaderId;
    private List<Integer> light_effect;
    private List<Integer> special_icon;
    private int mount_icon;
    private int vip;
    private int moveSpeed;
    private int fly_type;
    private int portrait;
    private int weapon;
    private int gem;
    private List<FiedValue> list;
    
    public RespAppear() {
        this.serial = 1;
        this.partyName = "";
        this.job = 0;
        this.relive = 0;
        this.level = 0;
        this.leaderId = 0;
        this.light_effect = new ArrayList<>();
        this.special_icon = new ArrayList<>();
        this.mount_icon = 0;
        this.vip = 0;
        this.moveSpeed = 0;
        this.fly_type = 0;
        this.portrait = 0;
        this.weapon = 0;
        this.gem = 0;
    }
    
    public int getRoleId() {
        return this.roleId;
    }
    
    public int getPosX() {
        return this.posX;
    }
    
    public int getPosY() {
        return this.posY;
    }
    
    public int getSerial() {
        return this.serial;
    }
    
    public String getPartyName() {
        return this.partyName;
    }
    
    public byte getJob() {
        return this.job;
    }
    
    public String getName() {
        return this.name;
    }
    
    public int getRelive() {
        return this.relive;
    }
    
    public int getLevel() {
        return this.level;
    }
    
    public int getTitle() {
        return this.title;
    }
    
    public int getLeaderId() {
        return this.leaderId;
    }
    
    public List<Integer> getLight_effect() {
        return this.light_effect;
    }
    
    public List<Integer> getSpecial_icon() {
        return this.special_icon;
    }
    
    public int getMount_icon() {
        return this.mount_icon;
    }
    
    public int getVip() {
        return this.vip;
    }
    
    public int getMoveSpeed() {
        return this.moveSpeed;
    }
    
    public int getFly_type() {
        return this.fly_type;
    }
    
    public int getPortrait() {
        return this.portrait;
    }
    
    public int getWeapon() {
        return this.weapon;
    }
    
    public int getGem() {
        return this.gem;
    }
    
    public List<FiedValue> getList() {
        return this.list;
    }
    
    public void setRoleId(final int roleId) {
        this.roleId = roleId;
    }
    
    public void setPosX(final int posX) {
        this.posX = posX;
    }
    
    public void setPosY(final int posY) {
        this.posY = posY;
    }
    
    public void setSerial(final int serial) {
        this.serial = serial;
    }
    
    public void setPartyName(final String partyName) {
        this.partyName = partyName;
    }
    
    public void setJob(final byte job) {
        this.job = job;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public void setRelive(final int relive) {
        this.relive = relive;
    }
    
    public void setLevel(final int level) {
        this.level = level;
    }
    
    public void setTitle(final int title) {
        this.title = title;
    }
    
    public void setLeaderId(final int leaderId) {
        this.leaderId = leaderId;
    }
    
    public void setLight_effect(final List<Integer> light_effect) {
        this.light_effect = light_effect;
    }
    
    public void setSpecial_icon(final List<Integer> special_icon) {
        this.special_icon = special_icon;
    }
    
    public void setMount_icon(final int mount_icon) {
        this.mount_icon = mount_icon;
    }
    
    public void setVip(final int vip) {
        this.vip = vip;
    }
    
    public void setMoveSpeed(final int moveSpeed) {
        this.moveSpeed = moveSpeed;
    }
    
    public void setFly_type(final int fly_type) {
        this.fly_type = fly_type;
    }
    
    public void setPortrait(final int portrait) {
        this.portrait = portrait;
    }
    
    public void setWeapon(final int weapon) {
        this.weapon = weapon;
    }
    
    public void setGem(final int gem) {
        this.gem = gem;
    }
    
    public void setList(final List<FiedValue> list) {
        this.list = list;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof RespAppear)) {
            return false;
        }
        RespAppear other = (RespAppear)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getRoleId() != other.getRoleId()) {
            return false;
        }
        if (this.getPosX() != other.getPosX()) {
            return false;
        }
        if (this.getPosY() != other.getPosY()) {
            return false;
        }
        if (this.getSerial() != other.getSerial()) {
            return false;
        }
        Object this$partyName = this.getPartyName();
        Object other$partyName = other.getPartyName();
        if (!((this$partyName != null) ? this$partyName.equals(other$partyName) : (other$partyName == null))) {
            return false;
        }
        if (this.getJob() != other.getJob()) {
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
        if (this.getTitle() != other.getTitle()) {
            return false;
        }
        if (this.getLeaderId() != other.getLeaderId()) {
            return false;
        }
        Object this$light_effect = this.getLight_effect();
        Object other$light_effect = other.getLight_effect();
        if (!((this$light_effect != null) ? this$light_effect.equals(other$light_effect) : (other$light_effect == null))) {
            return false;
        }
        Object this$special_icon = this.getSpecial_icon();
        Object other$special_icon = other.getSpecial_icon();
        if (!((this$special_icon != null) ? this$special_icon.equals(other$special_icon) : (other$special_icon == null))) {
            return false;
        }
        if (this.getMount_icon() != other.getMount_icon()) {
            return false;
        }
        if (this.getVip() != other.getVip()) {
            return false;
        }
        if (this.getMoveSpeed() != other.getMoveSpeed()) {
            return false;
        }
        if (this.getFly_type() != other.getFly_type()) {
            return false;
        }
        if (this.getPortrait() != other.getPortrait()) {
            return false;
        }
        if (this.getWeapon() != other.getWeapon()) {
            return false;
        }
        if (this.getGem() != other.getGem()) {
            return false;
        }
        Object this$list = this.getList();
        Object other$list = other.getList();
        return (this$list != null) ? this$list.equals(other$list) : (other$list == null);
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof RespAppear;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getRoleId();
        result = result * 59 + this.getPosX();
        result = result * 59 + this.getPosY();
        result = result * 59 + this.getSerial();
        Object $partyName = this.getPartyName();
        result = result * 59 + (($partyName == null) ? 43 : $partyName.hashCode());
        result = result * 59 + this.getJob();
        Object $name = this.getName();
        result = result * 59 + (($name == null) ? 43 : $name.hashCode());
        result = result * 59 + this.getRelive();
        result = result * 59 + this.getLevel();
        result = result * 59 + this.getTitle();
        result = result * 59 + this.getLeaderId();
        Object $light_effect = this.getLight_effect();
        result = result * 59 + (($light_effect == null) ? 43 : $light_effect.hashCode());
        Object $special_icon = this.getSpecial_icon();
        result = result * 59 + (($special_icon == null) ? 43 : $special_icon.hashCode());
        result = result * 59 + this.getMount_icon();
        result = result * 59 + this.getVip();
        result = result * 59 + this.getMoveSpeed();
        result = result * 59 + this.getFly_type();
        result = result * 59 + this.getPortrait();
        result = result * 59 + this.getWeapon();
        result = result * 59 + this.getGem();
        Object $list = this.getList();
        result = result * 59 + (($list == null) ? 43 : $list.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "RespAppear(roleId=" + this.getRoleId() + ", posX=" + this.getPosX() + ", posY=" + this.getPosY() + ", serial=" + this.getSerial() + ", partyName=" + this.getPartyName() + ", job=" + (int)this.getJob() + ", name=" + this.getName() + ", relive=" + this.getRelive() + ", level=" + this.getLevel() + ", title=" + this.getTitle() + ", leaderId=" + this.getLeaderId() + ", light_effect=" + this.getLight_effect() + ", special_icon=" + this.getSpecial_icon() + ", mount_icon=" + this.getMount_icon() + ", vip=" + this.getVip() + ", moveSpeed=" + this.getMoveSpeed() + ", fly_type=" + this.getFly_type() + ", portrait=" + this.getPortrait() + ", weapon=" + this.getWeapon() + ", gem=" + this.getGem() + ", list=" + this.getList() + ")";
    }
}
