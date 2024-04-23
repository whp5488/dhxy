package com.kitty.system.game.fightnew.utils;

import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.mina.message.Message;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.common.enter.FieldValue;
import com.kitty.system.enums.war.itempro.def.PET_PASSIVE_SKILL_TRIGGER_DICT;
import com.kitty.system.enums.war.wardef.FIGHT_TYPE;
import com.kitty.system.game.broadcast.BroadcastService;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.bean.PetFightObject;
import com.kitty.system.game.fightnew.bean.PlayerFightObject;
import com.kitty.system.game.fightnew.book.pet.PetPassiveSkillUtil;
import com.kitty.system.game.fightnew.book.pet.utils.PetTriggerPassiveSkill;
import com.kitty.system.game.fightnew.messages.RespFightInParam;
import com.kitty.system.game.fightnew.messages.old.RespEndCombat;
import com.kitty.system.game.fightnew.messages.old.RespFightShouting;
import com.kitty.system.game.fightnew.messages.vo.CombatAddObject;
import com.kitty.system.game.fightnew.messages.vo.FightMember;
import com.kitty.system.game.fightnew.model.ActionDetailParam;
import com.kitty.system.game.fightnew.model.FightObjectParam;
import com.kitty.system.game.fightnew.service.FightExecutorNewService;
import com.kitty.system.game.fightnew.service.FightMessageNewService;
import com.kitty.system.game.fightnew.service.FightNewService;
import com.kitty.system.game.fightnew.substitution.SubstitutionHandler;
import com.kitty.system.game.fightnew.substitution.service.SubstitutionService;
import com.kitty.system.game.pet.domain.Pet;
import com.kitty.system.game.pet.service.PetService;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.role.service.RoleService;
import com.kitty.system.game.skill.domain.RoleSkill;
import com.kitty.system.game.vo.utils.StatusConst;
import com.kitty.system.init.domain.SysPetSkill;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class FightUtils {
  public static void substitution(Fight fight) {
    if (!isSubstitution(fight))
      return; 
    FightNewService fightNewService = (FightNewService)SpringUtils.getBean(FightNewService.class);
    Map<Boolean, List<FightObject>> disappearMap = disappearObject(fight);
    if (disappearMap != null && disappearMap.size() > 0) {
      fightNewService.sendFightParams(fight, false);
      for (Map.Entry<Boolean, List<FightObject>> entry : disappearMap.entrySet()) {
        List<FightObject> fightObjects = entry.getValue();
        if (fightObjects != null && fightObjects.size() > 0)
          for (FightObject fightObject : fightObjects)
            MessagePusher.pushMessage(fightObject.getRole(), (Message)new RespEndCombat());  
      } 
    } 
    List<FightObject> addObjs = objectAppear(fight, disappearMap);
    newFightObject(fight, addObjs);
    if (addObjs != null && addObjs.size() > 0)
      fightNewService.sendFightParams(fight, true); 
  }
  
  private static boolean isSubstitution(Fight fight) {
    if (fight.isPvp() != 1)
      return false; 
    return true;
  }
  
  private static Map<Boolean, List<FightObject>> disappearObject(Fight fight) {
    Map<Boolean, List<FightObject>> returnMap = new HashMap<>();
    List<FightObject> removeList = new ArrayList<>();
    List<FightObject> removeA = getRemoveList(fight.getListA());
    if (removeA != null) {
      removeList.addAll(removeA);
      returnMap.put(Boolean.valueOf(true), removeA);
    } 
    List<FightObject> removeB = getRemoveList(fight.getListB());
    if (removeB != null) {
      removeList.addAll(removeB);
      returnMap.put(Boolean.valueOf(false), removeB);
    } 
    FightNewService fightNewService = (FightNewService)SpringUtils.getBean(FightNewService.class);
    for (FightObject obj : removeList) {
      fight.addExitRole(Boolean.valueOf(obj.isASide()), new FieldValue(1, obj.getId().intValue()));
      Role role = obj.getRole();
      if (role != null) {
        fightNewService.excuteTeamDisappear(fight, role, obj);
        FightExecutorNewService.FIGHT_ROOMS.remove(Integer.valueOf(obj.getFightPetId()));
        fightNewService.deleteRoleFightData(fight, obj.getId());
        role.setLastDead(0);
        fight.removeFightObject(obj);
        ActionParamUtils.excuteDisappear(fight, obj, obj.getId().intValue(), 46, 0, 1);
      } 
    } 
    return returnMap;
  }
  
  private static boolean isRemove(List<FightObject> list, FightObject obj) {
    for (FightObject fightObject : list) {
      if (!fightObject.isFallDown() && obj.getId().equals(Integer.valueOf(fightObject.getHostId())))
        return false; 
    } 
    return true;
  }
  
  private static List<FightObject> getRemoveList(List<FightObject> list) {
    List<FightObject> remove = new ArrayList<>();
    if (list != null && list.size() > 0)
      for (FightObject obj : list) {
        if (obj.isPlayer() && obj.isFallDown() && isRemove(list, obj))
          remove.add(obj); 
      }  
    if (remove.size() > 0)
      return remove; 
    return null;
  }
  
  private static List<FightObject> objectAppear(Fight fight, Map<Boolean, List<FightObject>> disappearMap) {
    if (disappearMap != null) {
      List<FightObject> disA = disappearMap.get(Boolean.valueOf(true));
      List<FightObject> addObjA = completeFight(fight, fight.getListA(), fight.getFightMemberA(), disA, true);
      List<FightObject> disB = disappearMap.get(Boolean.valueOf(false));
      List<FightObject> addObjB = completeFight(fight, fight.getListB(), fight.getFightMemberB(), disB, false);
      List<FightObject> list = new ArrayList<>();
      if (addObjA != null && addObjA.size() > 0)
        list.addAll(addObjA); 
      if (addObjB != null && addObjB.size() > 0)
        list.addAll(addObjB); 
      if (list.size() > 0)
        return list; 
    } 
    return null;
  }
  
  private static Role getRole(Fight fight, boolean isA) {
    SubstitutionHandler substitutionHandler = SubstitutionService.querySubstitution(FIGHT_TYPE.CS_SUB.getValue());
    if (substitutionHandler != null) {
      Role role = substitutionHandler.getRole(fight, isA);
      if (role != null)
        return role; 
    } 
    return null;
  }
  
  private static List<FightObject> completeFight(Fight fight, List<FightObject> fightObjects, List<FightMember> fightMembers, List<FightObject> disappears, boolean isA) {
    List<FightObject> listObjects = new ArrayList<>();
    if (disappears != null && disappears.size() > 0)
      for (FightObject obj : disappears) {
        Role role = getRole(fight, isA);
        if (role != null) {
          FightObject fightObject = createFightObject(fight, fightObjects, fightMembers, role, (PlayerFightObject)obj);
          if (fightObject != null)
            listObjects.add(fightObject); 
        } 
      }  
    return listObjects;
  }
  
  private static FightObject createFightObject(Fight fight, List<FightObject> fightObjects, List<FightMember> listFightMembers, Role role, PlayerFightObject playerObj) {
    PlayerFightObject playerFightObject = new PlayerFightObject(role, playerObj.getIndex(), playerObj.getCustomIndex(), true);
    fightObjects.add(playerFightObject);
    FightMember fightMember = playerFightObject.asFightMember();
    listFightMembers.add(fightMember);
    FightExecutorNewService.FIGHT_ROOMS.put(role.getRoleId(), Integer.valueOf(fight.getId()));
    enterFightMsg(fight, role, (FightObject)playerFightObject, (FightObject)playerFightObject);
    Pet pet = (Pet)((PetService)SpringUtils.getBean(PetService.class)).selectPet(role).get(role.getExtendBox().getCombatPet());
    if (pet != null) {
      PetFightObject petFightObj = new PetFightObject(pet, (FightObject)playerFightObject);
      fightObjects.add(petFightObj);
      FightMember petFightMember = petFightObj.asFightMember();
      listFightMembers.add(petFightMember);
      playerFightObject.setFightPetId(petFightObj.getId().intValue());
      FightExecutorNewService.PETID_ROLEID.put(pet.getPetId(), role.getRoleId());
      FightExecutorNewService.FIGHT_ROOMS.put(pet.getPetId(), Integer.valueOf(fight.getId()));
      enterFightMsg(fight, role, (FightObject)playerFightObject, (FightObject)petFightObj);
    } 
    ((FightNewService)SpringUtils.getBean(FightNewService.class)).fullLifeRoleAndPet(fight, role);
    ((FightNewService)SpringUtils.getBean(FightNewService.class)).checkAtFightStart(role);
    return (FightObject)playerFightObject;
  }
  
  private static void enterFightMsg(Fight fight, Role role, FightObject effector, FightObject target) {
    ActionDetailParam actionDetailParam = new ActionDetailParam(effector, 2, 0, target.getId().intValue());
    actionDetailParam.setRoleId(role.getId().longValue());
    List<CombatAddObject> list = new ArrayList<>();
    list.add(((FightNewService)SpringUtils.getBean(FightNewService.class)).addObject2Fight(effector.getId().intValue(), target, fight));
    actionDetailParam.setCombatAddObjects(list);
    RespFightShouting fightShouting = ((FightMessageNewService)SpringUtils.getBean(FightMessageNewService.class)).fightShouting(fight.queryFightObject(target.getId()), "战斗出场");
    FightObjectParam fightObjectParam = actionDetailParam.gainTargetParam(target.getId().intValue(), true);
    fightObjectParam.setFightShouting(fightShouting);
    fight.getActionParam().getActionDetailParams().add(actionDetailParam);
  }
  
  private static void newFightObject(Fight fight, List<FightObject> addObjs) {
    if (addObjs != null && addObjs.size() > 0)
      for (FightObject obj : addObjs) {
        RespFightInParam respFightInParam = ((FightNewService)SpringUtils.getBean(FightNewService.class)).getRoundFightParam(obj.getRole(), fight, obj);
        respFightInParam.setFightType(fight.getType());
        respFightInParam.setId(String.valueOf(fight.getId()));
        FightMessageUtil.sendFightPackage(obj.getRole().getId().longValue(), (Message)respFightInParam);
        ((BroadcastService)SpringUtils.getBean(BroadcastService.class)).sendTitleInfoNew(obj.getRole());
      }  
  }
  
  public static Map<Boolean, List<Role>> substitutionQuiteFight(Fight fight) {
    if (isSubstitution(fight) && fight.getExitRoles().size() > 0) {
      List<Role> rolesA = quiteFight(fight, true);
      List<Role> rolesB = quiteFight(fight, false);
      Map<Boolean, List<Role>> map = new HashMap<>();
      map.put(Boolean.valueOf(true), rolesA);
      map.put(Boolean.valueOf(false), rolesB);
      return map;
    } 
    return null;
  }
  
  private static List<Role> quiteFight(Fight fight, boolean isA) {
    Set<FieldValue> fv = (Set<FieldValue>)fight.getExitRoles().get(Boolean.valueOf(isA));
    if (fv != null && fv.size() > 0) {
      List<Role> roles = new ArrayList<>();
      for (FieldValue fieldValue : fv) {
        if (fieldValue.getK() == 2)
          continue; 
        Role role = ((RoleService)SpringUtils.getBean(RoleService.class)).getPlayer(fieldValue.getV());
        if (role == null)
          continue; 
        roles.add(role);
      } 
      if (roles.size() > 0)
        return roles; 
    } 
    return null;
  }
  
  public static void excutePetDisappear(Fight fight, FightObject effector, FightObject delPet) {
    if (delPet != null) {
      delPet.setCurrLife(0);
      List<Integer> statusList = new ArrayList<>();
      for (Iterator<Integer> iterator = StatusConst.getPositiveStatus().iterator(); iterator.hasNext(); ) {
        int status = ((Integer)iterator.next()).intValue();
        if (delPet.getStatusValue(status) > 0)
          statusList.add(Integer.valueOf(status)); 
      } 
      PassiveSkillUtils.exitOrClearPositive(fight, effector, delPet, statusList);
      fight.excuteExitPassive(delPet);
      fight.petDie(delPet);
      PetPassiveSkillUtil.sendDisappearData(fight, delPet);
    } 
  }
  
  public static RoleSkill getPetRoleSkill(int skillId) {
    SysPetSkill petSkill = (SysPetSkill)((Map)PetTriggerPassiveSkill.data_pet_skill_maps.get(PET_PASSIVE_SKILL_TRIGGER_DICT.INITIATIVE_SKILL.getValue())).get(Integer.valueOf(skillId));
    if (petSkill != null) {
      RoleSkill roleSkill = new RoleSkill();
      roleSkill.setSkillId(petSkill.getSerial());
      roleSkill.setType(petSkill.getAttr());
      return roleSkill;
    } 
    return null;
  }
}
