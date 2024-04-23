package com.kitty.system.game.fightnew.model;

import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.messages.vo.CombatAddObject;
import com.kitty.system.game.skill.domain.RoleSkill;
import com.kitty.system.game.vo.FieldByte;
import java.beans.ConstructorProperties;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ActionDetailParam {
  public void setType(int type) {
    this.type = type;
  }
  
  public void setSkillSerial(int skillSerial) {
    this.skillSerial = skillSerial;
  }
  
  public void setColorId(byte colorId) {
    this.colorId = colorId;
  }
  
  public void setAttackerId(int attackerId) {
    this.attackerId = attackerId;
  }
  
  public void setRoleId(long roleId) {
    this.roleId = roleId;
  }
  
  public void setTargetId(int targetId) {
    this.targetId = targetId;
  }
  
  public void setAttackerParam(List<FightObjectParam> attackerParam) {
    this.attackerParam = attackerParam;
  }
  
  public void setTargetParamList(List<FightObjectParam> targetParamList) {
    this.targetParamList = targetParamList;
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
  
  @ConstructorProperties({"type", "skillSerial", "colorId", "attackerId", "roleId", "targetId", "attackerParam", "targetParamList", "petList", "combatAddObjects", "hfType"})
  public ActionDetailParam(int type, int skillSerial, byte colorId, int attackerId, long roleId, int targetId, List<FightObjectParam> attackerParam, List<FightObjectParam> targetParamList, List<RefreshPet> petList, List<CombatAddObject> combatAddObjects, byte hfType) {
    this.type = type;
    this.skillSerial = skillSerial;
    this.colorId = colorId;
    this.attackerId = attackerId;
    this.roleId = roleId;
    this.targetId = targetId;
    this.attackerParam = attackerParam;
    this.targetParamList = targetParamList;
    this.petList = petList;
    this.combatAddObjects = combatAddObjects;
    this.hfType = hfType;
  }
  
  private int type = -1;
  
  public int getType() {
    return this.type;
  }
  
  private int skillSerial = -1;
  
  public int getSkillSerial() {
    return this.skillSerial;
  }
  
  private byte colorId = 0;
  
  private int attackerId;
  
  private long roleId;
  
  private int targetId;
  
  private List<FightObjectParam> attackerParam;
  
  public byte getColorId() {
    return this.colorId;
  }
  
  public int getAttackerId() {
    return this.attackerId;
  }
  
  public long getRoleId() {
    return this.roleId;
  }
  
  public int getTargetId() {
    return this.targetId;
  }
  
  public List<FightObjectParam> getAttackerParam() {
    return this.attackerParam;
  }
  
  private List<FightObjectParam> targetParamList = new ArrayList<>();
  
  private List<RefreshPet> petList;
  
  private List<CombatAddObject> combatAddObjects;
  
  private byte hfType;
  
  public List<FightObjectParam> getTargetParamList() {
    return this.targetParamList;
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
  
  public ActionDetailParam(FightObject effector, int type, int skillSerial) {
    this.type = type;
    this.skillSerial = skillSerial;
    this.attackerId = effector.getId().intValue();
    addHfType(effector, skillSerial);
  }
  
  public ActionDetailParam(FightObject effector, int type, int skillSerial, int targetId) {
    this.type = type;
    this.skillSerial = skillSerial;
    this.attackerId = effector.getId().intValue();
    this.targetId = targetId;
    addHfType(effector, skillSerial);
  }
  
  public ActionDetailParam(int type, int skillSerial, int effectorId, int targetId) {
    this.type = type;
    this.skillSerial = skillSerial;
    this.attackerId = effectorId;
    this.targetId = targetId;
  }
  
  public void addHfType(FightObject effector, int skillSerial) {
    if (effector != null && skillSerial > 0) {
      Optional<RoleSkill> opt = effector.getSkillIds().stream().filter(obj -> obj.getSkillId().equals(Integer.valueOf(skillSerial))).findFirst();
      if (opt.isPresent()) {
        RoleSkill roleSkill = opt.get();
        if (roleSkill != null) {
          List<FieldByte> effects = roleSkill.getEffects();
          for (FieldByte fieldByte : effects) {
            if (fieldByte.getV().byteValue() == 2 && 
              fieldByte.getK() != null) {
              this.hfType = (byte)(fieldByte.getK().intValue() % 10);
              break;
            } 
          } 
        } 
      } 
    } 
  }
  
  public void addAttackerParam(FightObjectParam fightObjectParam) {
    if (this.attackerParam == null)
      this.attackerParam = new ArrayList<>(); 
    this.attackerParam.add(fightObjectParam);
  }
  
  public FightObjectParam gainAttackerParam() {
    FightObjectParam fightObjectParam;
    if (this.attackerParam == null || this.attackerParam.size() <= 0) {
      this.attackerParam = new ArrayList<>();
      fightObjectParam = new FightObjectParam();
      fightObjectParam.setType(this.type);
      fightObjectParam.setObjId(this.attackerId);
      addAttackerParam(fightObjectParam);
    } else {
      fightObjectParam = this.attackerParam.get(0);
    } 
    return fightObjectParam;
  }
  
  public FightObjectParam gainTargetParam(int targetId, boolean isCreate) {
    FightObjectParam targetParam = null;
    for (int i = this.targetParamList.size() - 1; i >= 0; i--) {
      FightObjectParam param = this.targetParamList.get(i);
      if (param != null && targetId == param.getObjId()) {
        targetParam = param;
        break;
      } 
    } 
    if (isCreate && targetParam == null) {
      targetParam = new FightObjectParam();
      int size = this.targetParamList.size();
      targetParam.setOrder(++size);
      targetParam.setObjId(targetId);
      this.targetParamList.add(targetParam);
    } 
    return targetParam;
  }
  
  public FightObjectParam gainTargetParamZ(int targetId, boolean isCreate) {
    FightObjectParam targetParam = null;
    for (FightObjectParam param : this.targetParamList) {
      if (param != null && targetId == param.getObjId()) {
        targetParam = param;
        break;
      } 
    } 
    if (isCreate && targetParam == null) {
      targetParam = new FightObjectParam();
      int size = this.targetParamList.size();
      targetParam.setOrder(++size);
      targetParam.setObjId(targetId);
      this.targetParamList.add(targetParam);
    } 
    return targetParam;
  }
  
  public List<FightObjectParam> gainTargetParamList(int targetId, boolean isCreate) {
    List<FightObjectParam> targetParams = new ArrayList<>();
    for (FightObjectParam param : this.targetParamList) {
      if (param != null && targetId == param.getObjId())
        targetParams.add(param); 
    } 
    if (isCreate && targetParams.size() <= 0) {
      FightObjectParam targetParam = new FightObjectParam();
      int size = this.targetParamList.size();
      targetParam.setOrder(++size);
      targetParam.setObjId(targetId);
      targetParams.add(targetParam);
    } 
    return targetParams;
  }
  
  public String toString() {
    return "ActionDetailParam{type=" + this.type + ", skillSerial=" + this.skillSerial + ", attackerId=" + this.attackerId + ", targetId=" + this.targetId + ", attackerParam=" + this.attackerParam + ", targetParamList=" + this.targetParamList + '}';
  }
  
  public ActionDetailParam() {}
}
