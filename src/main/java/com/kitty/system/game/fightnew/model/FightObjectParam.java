
package com.kitty.system.game.fightnew.model;

import java.beans.ConstructorProperties;
import java.util.HashMap;
import java.util.Collection;
import java.util.ArrayList;
import com.kitty.system.game.fightnew.messages.old.RespFightShouting;
import java.util.List;
import java.util.Map;

public class FightObjectParam
{
    private int type;
    private int objId;
    private int changeLife;
    private int changeMana;
    private int currentLife;
    private int currentMana;
    private int maxLife;
    private int maxMana;
    private Map<Integer, Integer> statusMap;
    private List<Integer> clearStatus;
    private int specialStatus;
    private boolean isHit;
    private List<Integer> damageType;
    private int missed;
    private RespFightShouting fightShouting;
    private int disappear;
    private int guardTarget;
    private int order;
    
    public void addClearStatus(final int status) {
        if (status == 0) {
            return;
        }
        if (this.clearStatus == null) {
            this.clearStatus = new ArrayList<Integer>();
        }
        this.clearStatus.add(status);
    }
    
    public void addClearStatus(final List<Integer> status) {
        if (status == null || status.size() <= 0) {
            return;
        }
        if (this.clearStatus == null) {
            this.clearStatus = new ArrayList<Integer>();
        }
        this.clearStatus.addAll(status);
    }
    
    public Map<Integer, Integer> getStatusMap() {
        if (this.statusMap == null) {
            this.statusMap = new HashMap<Integer, Integer>();
        }
        return this.statusMap;
    }
    
    public void addDamageType(final int flag) {
        if (this.damageType == null) {
            this.damageType = new ArrayList<Integer>();
        }
        this.damageType.add(flag);
    }
    
    public void setType(final int type) {
        this.type = type;
    }
    
    public void setObjId(final int objId) {
        this.objId = objId;
    }
    
    public void setChangeLife(final int changeLife) {
        this.changeLife = changeLife;
    }
    
    public void setChangeMana(final int changeMana) {
        this.changeMana = changeMana;
    }
    
    public void setCurrentLife(final int currentLife) {
        this.currentLife = currentLife;
    }
    
    public void setCurrentMana(final int currentMana) {
        this.currentMana = currentMana;
    }
    
    public void setMaxLife(final int maxLife) {
        this.maxLife = maxLife;
    }
    
    public void setMaxMana(final int maxMana) {
        this.maxMana = maxMana;
    }
    
    public void setStatusMap(final Map<Integer, Integer> statusMap) {
        this.statusMap = statusMap;
    }
    
    public void setClearStatus(final List<Integer> clearStatus) {
        this.clearStatus = clearStatus;
    }
    
    public void setSpecialStatus(final int specialStatus) {
        this.specialStatus = specialStatus;
    }
    
    public void setHit(final boolean isHit) {
        this.isHit = isHit;
    }
    
    public void setDamageType(final List<Integer> damageType) {
        this.damageType = damageType;
    }
    
    public void setMissed(final int missed) {
        this.missed = missed;
    }
    
    public void setFightShouting(final RespFightShouting fightShouting) {
        this.fightShouting = fightShouting;
    }
    
    public void setDisappear(final int disappear) {
        this.disappear = disappear;
    }
    
    public void setGuardTarget(final int guardTarget) {
        this.guardTarget = guardTarget;
    }
    
    public void setOrder(final int order) {
        this.order = order;
    }
    
    public int getType() {
        return this.type;
    }
    
    public int getObjId() {
        return this.objId;
    }
    
    public int getChangeLife() {
        return this.changeLife;
    }
    
    public int getChangeMana() {
        return this.changeMana;
    }
    
    public int getCurrentLife() {
        return this.currentLife;
    }
    
    public int getCurrentMana() {
        return this.currentMana;
    }
    
    public int getMaxLife() {
        return this.maxLife;
    }
    
    public int getMaxMana() {
        return this.maxMana;
    }
    
    public List<Integer> getClearStatus() {
        return this.clearStatus;
    }
    
    public int getSpecialStatus() {
        return this.specialStatus;
    }
    
    public boolean isHit() {
        return this.isHit;
    }
    
    public List<Integer> getDamageType() {
        return this.damageType;
    }
    
    public int getMissed() {
        return this.missed;
    }
    
    public RespFightShouting getFightShouting() {
        return this.fightShouting;
    }
    
    public int getDisappear() {
        return this.disappear;
    }
    
    public int getGuardTarget() {
        return this.guardTarget;
    }
    
    public int getOrder() {
        return this.order;
    }
    
    public FightObjectParam() {
        this.isHit = true;
        this.missed = -1;
    }
    
    @ConstructorProperties({ "type", "objId", "changeLife", "changeMana", "currentLife", "currentMana", "maxLife", "maxMana", "statusMap", "clearStatus", "specialStatus", "isHit", "damageType", "missed", "fightShouting", "disappear", "guardTarget", "order" })
    public FightObjectParam(final int type, final int objId, final int changeLife, final int changeMana, final int currentLife, final int currentMana, final int maxLife, final int maxMana, final Map<Integer, Integer> statusMap, final List<Integer> clearStatus, final int specialStatus, final boolean isHit, final List<Integer> damageType, final int missed, final RespFightShouting fightShouting, final int disappear, final int guardTarget, final int order) {
        this.isHit = true;
        this.missed = -1;
        this.type = type;
        this.objId = objId;
        this.changeLife = changeLife;
        this.changeMana = changeMana;
        this.currentLife = currentLife;
        this.currentMana = currentMana;
        this.maxLife = maxLife;
        this.maxMana = maxMana;
        this.statusMap = statusMap;
        this.clearStatus = clearStatus;
        this.specialStatus = specialStatus;
        this.isHit = isHit;
        this.damageType = damageType;
        this.missed = missed;
        this.fightShouting = fightShouting;
        this.disappear = disappear;
        this.guardTarget = guardTarget;
        this.order = order;
    }
}
