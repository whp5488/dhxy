package com.kitty.system.game.fightnew.messages;

import java.util.ArrayList;
import com.kitty.system.game.fightnew.model.SkillParam;
import com.kitty.system.game.fightnew.messages.vo.ExcuteFightParam;
import com.kitty.system.game.enter.message.RespCombatOpponets;
import com.kitty.system.game.enter.message.RespCombatFriends;
import com.kitty.system.game.fightnew.model.RefreshPet;
import com.kitty.system.game.fightnew.model.PetFightInParam;
import java.util.List;
import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 3584)
public class RespFightInParam extends Message
{
    private byte type;
    private int fightType;
    private String id;
    private int roleId;
    private List<PetFightInParam> petFightInParam;
    private List<RefreshPet> petList;
    private RespCombatFriends respCombatFriends;
    private RespCombatOpponets respCombatOpponets;
    private List<ExcuteFightParam> excuteFightParams;
    private List<SkillParam> skillParams;
    
    public RespFightInParam() {
        this.type = 1;
        this.respCombatFriends = new RespCombatFriends();
        this.respCombatOpponets = new RespCombatOpponets();
    }
    
    public void addPetFightInParam(PetFightInParam petFightInParam) {
        if (this.petFightInParam == null) {
            this.petFightInParam = new ArrayList<>();
        }
        this.petFightInParam.add(petFightInParam);
    }
    
    @Override
    public String toString() {
        return "RespFightInParam{fightType=" + this.fightType + ", id='" + this.id + '\'' + ", roleId=" + this.roleId + ", petFightInParam=" + this.petFightInParam + ", petList=" + this.petList + ", respCombatFriends=" + this.respCombatFriends + ", respCombatOpponets=" + this.respCombatOpponets + ", excuteFightParams=" + this.excuteFightParams + ", skillParams=" + this.skillParams + '}';
    }
    
    public byte getType() {
        return this.type;
    }
    
    public int getFightType() {
        return this.fightType;
    }
    
    public String getId() {
        return this.id;
    }
    
    public int getRoleId() {
        return this.roleId;
    }
    
    public List<PetFightInParam> getPetFightInParam() {
        return this.petFightInParam;
    }
    
    public List<RefreshPet> getPetList() {
        return this.petList;
    }
    
    public RespCombatFriends getRespCombatFriends() {
        return this.respCombatFriends;
    }
    
    public RespCombatOpponets getRespCombatOpponets() {
        return this.respCombatOpponets;
    }
    
    public List<ExcuteFightParam> getExcuteFightParams() {
        return this.excuteFightParams;
    }
    
    public List<SkillParam> getSkillParams() {
        return this.skillParams;
    }
    
    public void setType(final byte type) {
        this.type = type;
    }
    
    public void setFightType(final int fightType) {
        this.fightType = fightType;
    }
    
    public void setId(final String id) {
        this.id = id;
    }
    
    public void setRoleId(final int roleId) {
        this.roleId = roleId;
    }
    
    public void setPetFightInParam(final List<PetFightInParam> petFightInParam) {
        this.petFightInParam = petFightInParam;
    }
    
    public void setPetList(final List<RefreshPet> petList) {
        this.petList = petList;
    }
    
    public void setRespCombatFriends(final RespCombatFriends respCombatFriends) {
        this.respCombatFriends = respCombatFriends;
    }
    
    public void setRespCombatOpponets(final RespCombatOpponets respCombatOpponets) {
        this.respCombatOpponets = respCombatOpponets;
    }
    
    public void setExcuteFightParams(final List<ExcuteFightParam> excuteFightParams) {
        this.excuteFightParams = excuteFightParams;
    }
    
    public void setSkillParams(final List<SkillParam> skillParams) {
        this.skillParams = skillParams;
    }
}
