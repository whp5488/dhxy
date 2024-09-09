package com.kitty.system.game.mirror.model;

import java.util.ArrayList;

public class RoleMirror
{
    private long id;
    private String name;
    private int level;
    private int defense;
    private short speed;
    private int magPower;
    private int phyPower;
    private int maxLife;
    private int maxMana;
    private ArrayList<Integer> skillIds;
    private int icon;
    private short polar;
    private int suitIcon;
    private int mountIcon;
    private int mountPosture;
    private int weaponIcon;
    private String partyName;
    private String title;
    private String message;
    
    public RoleMirror() {
    }
    
    public void setId(final long id) {
        this.id = id;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public void setLevel(final int level) {
        this.level = level;
    }
    
    public void setDefense(final int defense) {
        this.defense = defense;
    }
    
    public void setSpeed(final short speed) {
        this.speed = speed;
    }
    
    public void setMagPower(final int magPower) {
        this.magPower = magPower;
    }
    
    public void setPhyPower(final int phyPower) {
        this.phyPower = phyPower;
    }
    
    public void setMaxLife(final int maxLife) {
        this.maxLife = maxLife;
    }
    
    public void setMaxMana(final int maxMana) {
        this.maxMana = maxMana;
    }
    
    public void setSkillIds(final ArrayList<Integer> skillIds) {
        this.skillIds = skillIds;
    }
    
    public void setIcon(final int icon) {
        this.icon = icon;
    }
    
    public void setPolar(final short polar) {
        this.polar = polar;
    }
    
    public void setSuitIcon(final int suitIcon) {
        this.suitIcon = suitIcon;
    }
    
    public void setMountIcon(final int mountIcon) {
        this.mountIcon = mountIcon;
    }
    
    public void setMountPosture(final int mountPosture) {
        this.mountPosture = mountPosture;
    }
    
    public void setWeaponIcon(final int weaponIcon) {
        this.weaponIcon = weaponIcon;
    }
    
    public void setPartyName(final String partyName) {
        this.partyName = partyName;
    }
    
    public void setTitle(final String title) {
        this.title = title;
    }
    
    public void setMessage(final String message) {
        this.message = message;
    }
    
    public long getId() {
        return this.id;
    }
    
    public String getName() {
        return this.name;
    }
    
    public int getLevel() {
        return this.level;
    }
    
    public int getDefense() {
        return this.defense;
    }
    
    public short getSpeed() {
        return this.speed;
    }
    
    public int getMagPower() {
        return this.magPower;
    }
    
    public int getPhyPower() {
        return this.phyPower;
    }
    
    public int getMaxLife() {
        return this.maxLife;
    }
    
    public int getMaxMana() {
        return this.maxMana;
    }
    
    public ArrayList<Integer> getSkillIds() {
        return this.skillIds;
    }
    
    public int getIcon() {
        return this.icon;
    }
    
    public short getPolar() {
        return this.polar;
    }
    
    public int getSuitIcon() {
        return this.suitIcon;
    }
    
    public int getMountIcon() {
        return this.mountIcon;
    }
    
    public int getMountPosture() {
        return this.mountPosture;
    }
    
    public int getWeaponIcon() {
        return this.weaponIcon;
    }
    
    public String getPartyName() {
        return this.partyName;
    }
    
    public String getTitle() {
        return this.title;
    }
    
    public String getMessage() {
        return this.message;
    }
}
