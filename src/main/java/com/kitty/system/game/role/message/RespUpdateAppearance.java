package com.kitty.system.game.role.message;

import java.util.ArrayList;
import com.kitty.system.common.enter.FiedValue;
import java.util.List;
import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 61661)
public class RespUpdateAppearance extends Message
{
    private int roleId;
    private int serial;
    private String roleName;
    private int level;
    private int relive;
    private int weapon;
    private int gem;
    private int title;
    private int leaderId;
    private String partyName;
    private byte job;
    private int mount_icon;
    private int vip;
    private int moveSpeed;
    private int portrait;
    private List<Integer> light_effect;
    private List<Integer> special_icon;
    private int fly_type;
    private List<FiedValue> list;
    
    public RespUpdateAppearance() {
        this.weapon = 0;
        this.gem = 0;
        this.leaderId = 0;
        this.partyName = "";
        this.job = 0;
        this.mount_icon = 0;
        this.vip = 0;
        this.moveSpeed = 0;
        this.portrait = 0;
        this.light_effect = new ArrayList<>();
        this.special_icon = new ArrayList<>();
        this.fly_type = 0;
    }
    
    public void setRoleId(final int roleId) {
        this.roleId = roleId;
    }
    
    public void setSerial(final int serial) {
        this.serial = serial;
    }
    
    public void setRoleName(final String roleName) {
        this.roleName = roleName;
    }
    
    public void setLevel(final int level) {
        this.level = level;
    }
    
    public void setRelive(final int relive) {
        this.relive = relive;
    }
    
    public void setWeapon(final int weapon) {
        this.weapon = weapon;
    }
    
    public void setGem(final int gem) {
        this.gem = gem;
    }
    
    public void setTitle(final int title) {
        this.title = title;
    }
    
    public void setLeaderId(final int leaderId) {
        this.leaderId = leaderId;
    }
    
    public void setPartyName(final String partyName) {
        this.partyName = partyName;
    }
    
    public void setJob(final byte job) {
        this.job = job;
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
    
    public void setPortrait(final int portrait) {
        this.portrait = portrait;
    }
    
    public void setLight_effect(final List<Integer> light_effect) {
        this.light_effect = light_effect;
    }
    
    public void setSpecial_icon(final List<Integer> special_icon) {
        this.special_icon = special_icon;
    }
    
    public void setFly_type(final int fly_type) {
        this.fly_type = fly_type;
    }
    
    public void setList(final List<FiedValue> list) {
        this.list = list;
    }
    
    public int getRoleId() {
        return this.roleId;
    }
    
    public int getSerial() {
        return this.serial;
    }
    
    public String getRoleName() {
        return this.roleName;
    }
    
    public int getLevel() {
        return this.level;
    }
    
    public int getRelive() {
        return this.relive;
    }
    
    public int getWeapon() {
        return this.weapon;
    }
    
    public int getGem() {
        return this.gem;
    }
    
    public int getTitle() {
        return this.title;
    }
    
    public int getLeaderId() {
        return this.leaderId;
    }
    
    public String getPartyName() {
        return this.partyName;
    }
    
    public byte getJob() {
        return this.job;
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
    
    public int getPortrait() {
        return this.portrait;
    }
    
    public List<Integer> getLight_effect() {
        return this.light_effect;
    }
    
    public List<Integer> getSpecial_icon() {
        return this.special_icon;
    }
    
    public int getFly_type() {
        return this.fly_type;
    }
    
    public List<FiedValue> getList() {
        return this.list;
    }
}
