package com.kitty.system.game.fightnew.messages.vo;

import com.kitty.system.game.fightnew.model.RefreshPet;
import java.util.List;
import java.io.Serializable;

public class ExcuteFightParam implements Serializable
{
    private int type;
    private int skillId;
    private byte colorId;
    private int attaId;
    private long roleId;
    private List<RefreshPet> petList;
    private List<CombatAddObject> combatAddObjects;
    private byte hfType;
    private List<ExcuteFightObjectParam> attaParams;
    private List<ExcuteFightObjectParam> targetParams;
    
    public int getType() {
        return this.type;
    }
    
    public int getSkillId() {
        return this.skillId;
    }
    
    public byte getColorId() {
        return this.colorId;
    }
    
    public int getAttaId() {
        return this.attaId;
    }
    
    public long getRoleId() {
        return this.roleId;
    }
    
    public List<RefreshPet> getPetList() {
        return this.petList;
    }
    
    public List<CombatAddObject> getCombatAddObjects() {
        return this.combatAddObjects;
    }
    
    public byte getHfType() {
        return this.hfType;
    }
    
    public List<ExcuteFightObjectParam> getAttaParams() {
        return this.attaParams;
    }
    
    public List<ExcuteFightObjectParam> getTargetParams() {
        return this.targetParams;
    }
    
    public void setType(final int type) {
        this.type = type;
    }
    
    public void setSkillId(final int skillId) {
        this.skillId = skillId;
    }
    
    public void setColorId(final byte colorId) {
        this.colorId = colorId;
    }
    
    public void setAttaId(final int attaId) {
        this.attaId = attaId;
    }
    
    public void setRoleId(final long roleId) {
        this.roleId = roleId;
    }
    
    public void setPetList(final List<RefreshPet> petList) {
        this.petList = petList;
    }
    
    public void setCombatAddObjects(final List<CombatAddObject> combatAddObjects) {
        this.combatAddObjects = combatAddObjects;
    }
    
    public void setHfType(final byte hfType) {
        this.hfType = hfType;
    }
    
    public void setAttaParams(final List<ExcuteFightObjectParam> attaParams) {
        this.attaParams = attaParams;
    }
    
    public void setTargetParams(final List<ExcuteFightObjectParam> targetParams) {
        this.targetParams = targetParams;
    }
    
    public ExcuteFightParam(final int type, final int skillId, final byte colorId, final int attaId, final long roleId, final List<RefreshPet> petList, final List<CombatAddObject> combatAddObjects, final byte hfType, final List<ExcuteFightObjectParam> attaParams, final List<ExcuteFightObjectParam> targetParams) {
        this.skillId = -1;
        this.colorId = 0;
        this.hfType = 0;
        this.type = type;
        this.skillId = skillId;
        this.colorId = colorId;
        this.attaId = attaId;
        this.roleId = roleId;
        this.petList = petList;
        this.combatAddObjects = combatAddObjects;
        this.hfType = hfType;
        this.attaParams = attaParams;
        this.targetParams = targetParams;
    }
    
    public ExcuteFightParam() {
        this.skillId = -1;
        this.colorId = 0;
        this.hfType = 0;
    }
}
