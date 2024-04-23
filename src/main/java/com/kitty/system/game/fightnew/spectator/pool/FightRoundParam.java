package com.kitty.system.game.fightnew.spectator.pool;

import com.kitty.system.game.fightnew.messages.RespExcuteFight;
import com.kitty.system.game.fightnew.messages.RespRoundStartParam;
import com.kitty.system.game.fightnew.messages.vo.ExcuteFightParam;
import com.kitty.system.game.fightnew.messages.vo.FightMember;
import com.kitty.system.game.fightnew.model.PetFightInParam;
import com.kitty.system.game.fightnew.model.RefreshPet;
import com.kitty.system.game.fightnew.model.SkillParam;
import java.beans.ConstructorProperties;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FightRoundParam {
  private List<FightMember> fightMemberA;
  
  private List<FightMember> fightMemberB;
  
  private List<ExcuteFightParam> excuteFightParams;
  
  private Map<Integer, PetFightInParam> petFightInParamMap;
  
  private Map<Integer, List<RefreshPet>> petMap;
  
  private Map<Integer, List<Integer>> statusMap;
  
  private Map<Integer, List<SkillParam>> skillParamMap;
  
  private Map<Integer, RespRoundStartParam> respRoundStartParamMap;
  
  private RespExcuteFight respExcuteFight;
  
  public void setFightMemberA(List<FightMember> fightMemberA) {
    this.fightMemberA = fightMemberA;
  }
  
  public void setFightMemberB(List<FightMember> fightMemberB) {
    this.fightMemberB = fightMemberB;
  }
  
  public void setExcuteFightParams(List<ExcuteFightParam> excuteFightParams) {
    this.excuteFightParams = excuteFightParams;
  }
  
  public void setPetFightInParamMap(Map<Integer, PetFightInParam> petFightInParamMap) {
    this.petFightInParamMap = petFightInParamMap;
  }
  
  public void setPetMap(Map<Integer, List<RefreshPet>> petMap) {
    this.petMap = petMap;
  }
  
  public void setStatusMap(Map<Integer, List<Integer>> statusMap) {
    this.statusMap = statusMap;
  }
  
  public void setSkillParamMap(Map<Integer, List<SkillParam>> skillParamMap) {
    this.skillParamMap = skillParamMap;
  }
  
  public void setRespRoundStartParamMap(Map<Integer, RespRoundStartParam> respRoundStartParamMap) {
    this.respRoundStartParamMap = respRoundStartParamMap;
  }
  
  public void setRespExcuteFight(RespExcuteFight respExcuteFight) {
    this.respExcuteFight = respExcuteFight;
  }
  
  @ConstructorProperties({"fightMemberA", "fightMemberB", "excuteFightParams", "petFightInParamMap", "petMap", "statusMap", "skillParamMap", "respRoundStartParamMap", "respExcuteFight"})
  public FightRoundParam(List<FightMember> fightMemberA, List<FightMember> fightMemberB, List<ExcuteFightParam> excuteFightParams, Map<Integer, PetFightInParam> petFightInParamMap, Map<Integer, List<RefreshPet>> petMap, Map<Integer, List<Integer>> statusMap, Map<Integer, List<SkillParam>> skillParamMap, Map<Integer, RespRoundStartParam> respRoundStartParamMap, RespExcuteFight respExcuteFight) {
    this.fightMemberA = fightMemberA;
    this.fightMemberB = fightMemberB;
    this.excuteFightParams = excuteFightParams;
    this.petFightInParamMap = petFightInParamMap;
    this.petMap = petMap;
    this.statusMap = statusMap;
    this.skillParamMap = skillParamMap;
    this.respRoundStartParamMap = respRoundStartParamMap;
    this.respExcuteFight = respExcuteFight;
  }
  
  public FightRoundParam() {}
  
  public List<FightMember> getFightMemberA() {
    return this.fightMemberA;
  }
  
  public List<FightMember> getFightMemberB() {
    return this.fightMemberB;
  }
  
  public List<ExcuteFightParam> getExcuteFightParams() {
    return this.excuteFightParams;
  }
  
  public Map<Integer, PetFightInParam> getPetFightInParamMap() {
    return this.petFightInParamMap;
  }
  
  public Map<Integer, List<RefreshPet>> getPetMap() {
    return this.petMap;
  }
  
  public Map<Integer, List<Integer>> getStatusMap() {
    return this.statusMap;
  }
  
  public Map<Integer, List<SkillParam>> getSkillParamMap() {
    return this.skillParamMap;
  }
  
  public Map<Integer, RespRoundStartParam> getRespRoundStartParamMap() {
    return this.respRoundStartParamMap;
  }
  
  public RespExcuteFight getRespExcuteFight() {
    return this.respExcuteFight;
  }
  
  public PetFightInParam getPetFightInParam(int roleId) {
    if (this.petFightInParamMap != null && this.petFightInParamMap.get(Integer.valueOf(roleId)) != null)
      return this.petFightInParamMap.get(Integer.valueOf(roleId)); 
    return null;
  }
  
  public List<RefreshPet> getRefreshPets(int roleId) {
    if (this.petMap != null && this.petMap.get(Integer.valueOf(roleId)) != null)
      return this.petMap.get(Integer.valueOf(roleId)); 
    return null;
  }
  
  public List<Integer> getStatusList(int roleId) {
    if (this.statusMap != null && this.statusMap.get(Integer.valueOf(roleId)) != null)
      return this.statusMap.get(Integer.valueOf(roleId)); 
    return null;
  }
  
  public List<SkillParam> getSkillParamList(int roleId) {
    if (this.skillParamMap != null && this.skillParamMap.get(Integer.valueOf(roleId)) != null)
      return this.skillParamMap.get(Integer.valueOf(roleId)); 
    return null;
  }
  
  public void addSkillParam(int roleId, List<SkillParam> skillParam) {
    if (this.skillParamMap == null)
      this.skillParamMap = new HashMap<>(); 
    this.skillParamMap.put(Integer.valueOf(roleId), skillParam);
  }
  
  public void addPetFightInParam(int roleId, PetFightInParam petFightInParam) {
    if (this.petFightInParamMap == null)
      this.petFightInParamMap = new HashMap<>(); 
    this.petFightInParamMap.put(Integer.valueOf(roleId), petFightInParam);
  }
  
  public void addRefreshPet(int roleId, List<RefreshPet> refreshPets) {
    if (this.petMap == null)
      this.petMap = new HashMap<>(); 
    this.petMap.put(Integer.valueOf(roleId), refreshPets);
  }
  
  public void addStatus(int roleId, List<Integer> status) {
    if (this.statusMap == null)
      this.statusMap = new HashMap<>(); 
    this.statusMap.put(Integer.valueOf(roleId), status);
  }
}
