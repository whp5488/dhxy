package com.kitty.system.game.fightnew.messages.vo;

import com.kitty.system.game.fightnew.model.RefreshPet;
import java.beans.ConstructorProperties;
import java.io.Serializable;
import java.util.List;

public class ExcuteFightParam implements Serializable {
  private int type;
  
  public void setType(int type) {
    this.type = type;
  }
  
  public void setSkillId(int skillId) {
    this.skillId = skillId;
  }
  
  public void setColorId(byte colorId) {
    this.colorId = colorId;
  }
  
  public void setAttaId(int attaId) {
    this.attaId = attaId;
  }
  
  public void setRoleId(long roleId) {
    this.roleId = roleId;
  }
  
  public void setPetList(List<RefreshPet> petList) {
    this.petList = petList;
  }
  
  public void setCombatAddObjects(List<CombatAddObject> combatAddObjects) {
    this.combatAddObjects = combatAddObjects;
  }
  
  public void setHfType(byte hfType) {
    this.hfType = hfType;
  }
  
  public void setAttaParams(List<ExcuteFightObjectParam> attaParams) {
    this.attaParams = attaParams;
  }
  
  public void setTargetParams(List<ExcuteFightObjectParam> targetParams) {
    this.targetParams = targetParams;
  }
  
  @ConstructorProperties({"type", "skillId", "colorId", "attaId", "roleId", "petList", "combatAddObjects", "hfType", "attaParams", "targetParams"})
  public ExcuteFightParam(int type, int skillId, byte colorId, int attaId, long roleId, List<RefreshPet> petList, List<CombatAddObject> combatAddObjects, byte hfType, List<ExcuteFightObjectParam> attaParams, List<ExcuteFightObjectParam> targetParams) {
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
  
  public int getType() {
    return this.type;
  }
  
  private int skillId = -1;
  
  public int getSkillId() {
    return this.skillId;
  }
  
  private byte colorId = 0;
  
  private int attaId;
  
  private long roleId;
  
  private List<RefreshPet> petList;
  
  private List<CombatAddObject> combatAddObjects;
  
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
  
  private byte hfType = 0;
  
  private List<ExcuteFightObjectParam> attaParams;
  
  private List<ExcuteFightObjectParam> targetParams;
  
  public byte getHfType() {
    return this.hfType;
  }
  
  public List<ExcuteFightObjectParam> getAttaParams() {
    return this.attaParams;
  }
  
  public List<ExcuteFightObjectParam> getTargetParams() {
    return this.targetParams;
  }
  
  public ExcuteFightParam() {}
}
