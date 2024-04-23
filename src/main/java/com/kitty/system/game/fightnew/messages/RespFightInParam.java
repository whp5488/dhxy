package com.kitty.system.game.fightnew.messages;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;
import com.kitty.system.game.enter.message.RespCombatFriends;
import com.kitty.system.game.enter.message.RespCombatOpponets;
import com.kitty.system.game.fightnew.messages.vo.ExcuteFightParam;
import com.kitty.system.game.fightnew.model.PetFightInParam;
import com.kitty.system.game.fightnew.model.RefreshPet;
import com.kitty.system.game.fightnew.model.SkillParam;
import java.util.ArrayList;
import java.util.List;

@MessageMeta(module = 3584)
public class RespFightInParam extends Message {
  public void setType(byte type) {
    this.type = type;
  }
  
  public void setFightType(int fightType) {
    this.fightType = fightType;
  }
  
  public void setId(String id) {
    this.id = id;
  }
  
  public void setRoleId(int roleId) {
    this.roleId = roleId;
  }
  
  public void setPetFightInParam(List<PetFightInParam> petFightInParam) {
    this.petFightInParam = petFightInParam;
  }
  
  public void setPetList(List<RefreshPet> petList) {
    this.petList = petList;
  }
  
  public void setRespCombatFriends(RespCombatFriends respCombatFriends) {
    this.respCombatFriends = respCombatFriends;
  }
  
  public void setRespCombatOpponets(RespCombatOpponets respCombatOpponets) {
    this.respCombatOpponets = respCombatOpponets;
  }
  
  public void setExcuteFightParams(List<ExcuteFightParam> excuteFightParams) {
    this.excuteFightParams = excuteFightParams;
  }
  
  public void setSkillParams(List<SkillParam> skillParams) {
    this.skillParams = skillParams;
  }
  
  private byte type = 1;
  
  private int fightType;
  
  private String id;
  
  private int roleId;
  
  private List<PetFightInParam> petFightInParam;
  
  private List<RefreshPet> petList;
  
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
  
  private RespCombatFriends respCombatFriends = new RespCombatFriends();
  
  public RespCombatFriends getRespCombatFriends() {
    return this.respCombatFriends;
  }
  
  private RespCombatOpponets respCombatOpponets = new RespCombatOpponets();
  
  private List<ExcuteFightParam> excuteFightParams;
  
  private List<SkillParam> skillParams;
  
  public RespCombatOpponets getRespCombatOpponets() {
    return this.respCombatOpponets;
  }
  
  public List<ExcuteFightParam> getExcuteFightParams() {
    return this.excuteFightParams;
  }
  
  public List<SkillParam> getSkillParams() {
    return this.skillParams;
  }
  
  public void addPetFightInParam(PetFightInParam petFightInParam) {
    if (this.petFightInParam == null)
      this.petFightInParam = new ArrayList<>(); 
    this.petFightInParam.add(petFightInParam);
  }
  
  public String toString() {
    return "RespFightInParam{fightType=" + this.fightType + ", id='" + this.id + '\'' + ", roleId=" + this.roleId + ", petFightInParam=" + this.petFightInParam + ", petList=" + this.petList + ", respCombatFriends=" + this.respCombatFriends + ", respCombatOpponets=" + this.respCombatOpponets + ", excuteFightParams=" + this.excuteFightParams + ", skillParams=" + this.skillParams + '}';
  }
}
