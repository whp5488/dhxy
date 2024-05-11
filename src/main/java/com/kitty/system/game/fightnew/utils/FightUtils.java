
package com.kitty.system.game.fightnew.utils;

import com.kitty.system.enums.war.itempro.def.PET_PASSIVE_SKILL_TRIGGER_DICT;
import com.kitty.system.game.fightnew.book.pet.utils.PetTriggerPassiveSkill;
import com.kitty.system.init.domain.SysPetSkill;
import com.kitty.system.game.skill.domain.RoleSkill;
import com.kitty.system.game.fightnew.book.pet.PetPassiveSkillUtil;
import com.kitty.system.game.vo.utils.StatusConst;
import com.kitty.system.game.role.service.RoleService;
import java.util.Set;
import com.kitty.system.game.fightnew.messages.RespFightInParam;
import com.kitty.system.game.broadcast.BroadcastService;
import com.kitty.system.game.fightnew.model.FightObjectParam;
import com.kitty.system.game.fightnew.messages.old.RespFightShouting;
import com.kitty.system.game.fightnew.service.FightMessageNewService;
import com.kitty.system.game.fightnew.messages.vo.CombatAddObject;
import com.kitty.system.game.fightnew.model.ActionDetailParam;
import com.kitty.system.game.fightnew.bean.PetFightObject;
import com.kitty.system.game.pet.service.PetService;
import com.kitty.system.game.pet.domain.Pet;
import com.kitty.system.game.fightnew.bean.PlayerFightObject;
import com.kitty.system.game.fightnew.substitution.SubstitutionHandler;
import com.kitty.system.enums.war.wardef.FIGHT_TYPE;
import com.kitty.system.game.fightnew.substitution.service.SubstitutionService;
import com.kitty.system.game.fightnew.messages.vo.FightMember;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.fightnew.service.FightExecutorNewService;
import com.kitty.system.common.enter.FieldValue;
import java.util.Collection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import com.kitty.mina.message.Message;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.game.fightnew.messages.old.RespEndCombat;
import com.kitty.system.game.fightnew.FightObject;
import java.util.List;
import java.util.Map;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.system.game.fightnew.service.FightNewService;
import com.kitty.system.game.fightnew.bean.Fight;

public class FightUtils
{
    public static void substitution(final Fight fight) {
        if (!isSubstitution(fight)) {
            return;
        }
        final FightNewService fightNewService = SpringUtils.getBean(FightNewService.class);
        final Map<Boolean, List<FightObject>> disappearMap = disappearObject(fight);
        if (disappearMap != null && disappearMap.size() > 0) {
            fightNewService.sendFightParams(fight, false);
            for (final Map.Entry<Boolean, List<FightObject>> entry : disappearMap.entrySet()) {
                final List<FightObject> fightObjects = entry.getValue();
                if (fightObjects != null && fightObjects.size() > 0) {
                    for (final FightObject fightObject : fightObjects) {
                        MessagePusher.pushMessage(fightObject.getRole(), new RespEndCombat());
                    }
                }
            }
        }
        final List<FightObject> addObjs = objectAppear(fight, disappearMap);
        newFightObject(fight, addObjs);
        if (addObjs != null && addObjs.size() > 0) {
            fightNewService.sendFightParams(fight, true);
        }
    }
    
    private static boolean isSubstitution(final Fight fight) {
        return fight.isPvp() == 1;
    }
    
    private static Map<Boolean, List<FightObject>> disappearObject(final Fight fight) {
        final Map<Boolean, List<FightObject>> returnMap = new HashMap<Boolean, List<FightObject>>();
        final List<FightObject> removeList = new ArrayList<FightObject>();
        final List<FightObject> removeA = getRemoveList(fight.getListA());
        if (removeA != null) {
            removeList.addAll(removeA);
            returnMap.put(true, removeA);
        }
        final List<FightObject> removeB = getRemoveList(fight.getListB());
        if (removeB != null) {
            removeList.addAll(removeB);
            returnMap.put(false, removeB);
        }
        final FightNewService fightNewService = SpringUtils.getBean(FightNewService.class);
        for (final FightObject obj : removeList) {
            fight.addExitRole(obj.isASide(), new FieldValue(1, obj.getId()));
            final Role role = obj.getRole();
            if (role != null) {
                fightNewService.excuteTeamDisappear(fight, role, obj);
                FightExecutorNewService.FIGHT_ROOMS.remove(obj.getFightPetId());
                fightNewService.deleteRoleFightData(fight, obj.getId());
                role.setLastDead(0);
                fight.removeFightObject(obj);
                ActionParamUtils.excuteDisappear(fight, obj, obj.getId(), 46, 0, 1);
            }
        }
        return returnMap;
    }
    
    private static boolean isRemove(final List<FightObject> list, final FightObject obj) {
        for (final FightObject fightObject : list) {
            if (!fightObject.isFallDown() && obj.getId().equals(fightObject.getHostId())) {
                return false;
            }
        }
        return true;
    }
    
    private static List<FightObject> getRemoveList(final List<FightObject> list) {
        final List<FightObject> remove = new ArrayList<FightObject>();
        if (list != null && list.size() > 0) {
            for (final FightObject obj : list) {
                if (obj.isPlayer() && obj.isFallDown() && isRemove(list, obj)) {
                    remove.add(obj);
                }
            }
        }
        if (remove.size() > 0) {
            return remove;
        }
        return null;
    }
    
    private static List<FightObject> objectAppear(final Fight fight, final Map<Boolean, List<FightObject>> disappearMap) {
        if (disappearMap != null) {
            final List<FightObject> disA = disappearMap.get(true);
            final List<FightObject> addObjA = completeFight(fight, fight.getListA(), fight.getFightMemberA(), disA, true);
            final List<FightObject> disB = disappearMap.get(false);
            final List<FightObject> addObjB = completeFight(fight, fight.getListB(), fight.getFightMemberB(), disB, false);
            final List<FightObject> list = new ArrayList<FightObject>();
            if (addObjA != null && addObjA.size() > 0) {
                list.addAll(addObjA);
            }
            if (addObjB != null && addObjB.size() > 0) {
                list.addAll(addObjB);
            }
            if (list.size() > 0) {
                return list;
            }
        }
        return null;
    }
    
    private static Role getRole(final Fight fight, final boolean isA) {
        final SubstitutionService substitutionService = SpringUtils.getBean(SubstitutionService.class);
        final SubstitutionHandler substitutionHandler = SubstitutionService.querySubstitution(FIGHT_TYPE.CS_SUB.getValue());
        if (substitutionHandler != null) {
            final Role role = substitutionHandler.getRole(fight, isA);
            if (role != null) {
                return role;
            }
        }
        return null;
    }
    
    private static List<FightObject> completeFight(final Fight fight, final List<FightObject> fightObjects, final List<FightMember> fightMembers, final List<FightObject> disappears, final boolean isA) {
        final List<FightObject> listObjects = new ArrayList<FightObject>();
        if (disappears != null && disappears.size() > 0) {
            for (final FightObject obj : disappears) {
                final Role role = getRole(fight, isA);
                if (role != null) {
                    final FightObject fightObject = createFightObject(fight, fightObjects, fightMembers, role, (PlayerFightObject)obj);
                    if (fightObject == null) {
                        continue;
                    }
                    listObjects.add(fightObject);
                }
            }
        }
        return listObjects;
    }
    
    private static FightObject createFightObject(final Fight fight, final List<FightObject> fightObjects, final List<FightMember> listFightMembers, final Role role, final PlayerFightObject playerObj) {
        final FightObject fightObject = new PlayerFightObject(role, playerObj.getIndex(), playerObj.getCustomIndex(), true);
        fightObjects.add(fightObject);
        final FightMember fightMember = fightObject.asFightMember();
        listFightMembers.add(fightMember);
        FightExecutorNewService.FIGHT_ROOMS.put(role.getRoleId(), fight.getId());
        enterFightMsg(fight, role, fightObject, fightObject);
        final Pet pet = SpringUtils.getBean(PetService.class).selectPet(role).get(role.getExtendBox().getCombatPet());
        if (pet != null) {
            final PetFightObject petFightObj = new PetFightObject(pet, fightObject);
            fightObjects.add(petFightObj);
            final FightMember petFightMember = petFightObj.asFightMember();
            listFightMembers.add(petFightMember);
            fightObject.setFightPetId(petFightObj.getId());
            FightExecutorNewService.PETID_ROLEID.put(pet.getPetId(), role.getRoleId());
            FightExecutorNewService.FIGHT_ROOMS.put(pet.getPetId(), fight.getId());
            enterFightMsg(fight, role, fightObject, petFightObj);
        }
        SpringUtils.getBean(FightNewService.class).fullLifeRoleAndPet(fight, role);
        SpringUtils.getBean(FightNewService.class).checkAtFightStart(role);
        return fightObject;
    }
    
    private static void enterFightMsg(final Fight fight, final Role role, final FightObject effector, final FightObject target) {
        final ActionDetailParam actionDetailParam = new ActionDetailParam(effector, 2, 0, target.getId());
        actionDetailParam.setRoleId(role.getId());
        final List<CombatAddObject> list = new ArrayList<CombatAddObject>();
        list.add(SpringUtils.getBean(FightNewService.class).addObject2Fight(effector.getId(), target, fight));
        actionDetailParam.setCombatAddObjects(list);
        final RespFightShouting fightShouting = SpringUtils.getBean(FightMessageNewService.class).fightShouting(fight.queryFightObject(target.getId()), "战斗出场");
        final FightObjectParam fightObjectParam = actionDetailParam.gainTargetParam(target.getId(), true);
        fightObjectParam.setFightShouting(fightShouting);
        fight.getActionParam().getActionDetailParams().add(actionDetailParam);
    }
    
    private static void newFightObject(final Fight fight, final List<FightObject> addObjs) {
        if (addObjs != null && addObjs.size() > 0) {
            for (final FightObject obj : addObjs) {
                final RespFightInParam respFightInParam = SpringUtils.getBean(FightNewService.class).getRoundFightParam(obj.getRole(), fight, obj);
                respFightInParam.setFightType(fight.getType());
                respFightInParam.setId(String.valueOf(fight.getId()));
                FightMessageUtil.sendFightPackage(obj.getRole().getId(), respFightInParam);
                SpringUtils.getBean(BroadcastService.class).sendTitleInfoNew(obj.getRole());
            }
        }
    }
    
    public static Map<Boolean, List<Role>> substitutionQuiteFight(final Fight fight) {
        if (isSubstitution(fight) && fight.getExitRoles().size() > 0) {
            final List<Role> rolesA = quiteFight(fight, true);
            final List<Role> rolesB = quiteFight(fight, false);
            final Map<Boolean, List<Role>> map = new HashMap<Boolean, List<Role>>();
            map.put(true, rolesA);
            map.put(false, rolesB);
            return map;
        }
        return null;
    }
    
    private static List<Role> quiteFight(final Fight fight, final boolean isA) {
        final Set<FieldValue> fv = fight.getExitRoles().get(isA);
        if (fv != null && fv.size() > 0) {
            final List<Role> roles = new ArrayList<Role>();
            for (final FieldValue fieldValue : fv) {
                if (fieldValue.getK() == 2) {
                    continue;
                }
                final Role role = SpringUtils.getBean(RoleService.class).getPlayer(fieldValue.getV());
                if (role == null) {
                    continue;
                }
                roles.add(role);
            }
            if (roles.size() > 0) {
                return roles;
            }
        }
        return null;
    }
    
    public static void excutePetDisappear(final Fight fight, final FightObject effector, final FightObject delPet) {
        if (delPet != null) {
            delPet.setCurrLife(0);
            final List<Integer> statusList = new ArrayList<Integer>();
            for (final int status : StatusConst.getPositiveStatus()) {
                if (delPet.getStatusValue(status) > 0) {
                    statusList.add(status);
                }
            }
            PassiveSkillUtils.exitOrClearPositive(fight, effector, delPet, statusList);
            fight.excuteExitPassive(delPet);
            fight.petDie(delPet);
            PetPassiveSkillUtil.sendDisappearData(fight, delPet);
        }
    }
    
    public static RoleSkill getPetRoleSkill(final int skillId) {
        final SysPetSkill petSkill = (SysPetSkill)PetTriggerPassiveSkill.data_pet_skill_maps.get(PET_PASSIVE_SKILL_TRIGGER_DICT.INITIATIVE_SKILL.getValue()).get(skillId);
        if (petSkill != null) {
            final RoleSkill roleSkill = new RoleSkill();
            roleSkill.setSkillId(petSkill.getSerial());
            roleSkill.setType(petSkill.getAttr());
            return roleSkill;
        }
        return null;
    }
}
