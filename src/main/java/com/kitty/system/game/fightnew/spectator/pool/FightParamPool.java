
package com.kitty.system.game.fightnew.spectator.pool;

import java.util.HashMap;
import com.kitty.system.game.vo.utils.StatusConst;
import com.kitty.system.game.fightnew.bean.PetFightObject;
import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.listener.BaseGameEvent;
import com.kitty.listener.event.FightRoundParamEvent;
import com.kitty.listener.EventType;
import com.kitty.listener.EventDispatcher;
import com.kitty.system.game.fightnew.utils.FightBeforeActionUtil;
import com.kitty.system.game.fightnew.messages.RespRoundStartParam;
import com.kitty.system.game.fightnew.messages.RespFightInParam;
import com.kitty.system.game.fightnew.model.RefreshPet;
import com.kitty.system.game.fightnew.model.SkillParam;
import com.kitty.system.game.fightnew.model.PetFightInParam;
import com.kitty.system.game.pet.domain.Pet;
import com.kitty.system.game.role.domain.Role;
import java.util.Iterator;
import com.kitty.system.game.fightnew.messages.vo.ExcuteFightParam;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.system.game.fightnew.service.FightNewService;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightRecord.domain.FightObjectInfo;
import com.kitty.system.game.fightRecord.domain.FightSkillRecord;
import java.util.concurrent.ConcurrentHashMap;

public class FightParamPool
{
    public static ConcurrentHashMap<Integer, FightRoundParam> FIGHT_ROUND_PARAM;
    public static ConcurrentHashMap<Integer, FightSkillRecord> FIGHT_SKILL_RECORD;
    public static ConcurrentHashMap<Integer, FightObjectInfo> FIGHTOBJECT_INFO;
    
    public static void addFightInParam(final Fight fight) {
        final FightNewService fightNewService = SpringUtils.getBean(FightNewService.class);
        final FightRoundParam fightRoundParam = gainFightRoundParam(fight.getId());
        fightRoundParam.setFightMemberA(fightNewService.getFightMemberList(fight.getListA()));
        fightRoundParam.setFightMemberB(fightNewService.getFightMemberList(fight.getListB()));
        final List<ExcuteFightParam> excuteFightParams = fight.getFightStatus();
        gainFightRoundParam(fight.getId()).setExcuteFightParams(excuteFightParams);
        for (final FightObject roleObject : fight.getAllFightObjects()) {
            if (roleObject.isPlayer()) {
                final Role role = roleObject.getRole();
                final FightObject petObject = fight.queryFightObject(roleObject.getFightPetId());
                if (petObject != null) {
                    final Pet pet = petObject.getPet();
                    if (pet != null) {
                        final PetFightInParam petFightParam = fight.getPetFightParam(pet);
                        fightRoundParam.addPetFightInParam(role.getRoleId(), petFightParam);
                    }
                }
                final List<SkillParam> skillParams = fight.getSkillParams(role);
                if (skillParams != null) {
                    fightRoundParam.addSkillParam(role.getRoleId(), skillParams);
                }
                final List<RefreshPet> refreshPets = fightNewService.getFightInParamPetList(fight, roleObject);
                fightRoundParam.addRefreshPet(role.getRoleId(), refreshPets);
                if (roleObject.getStatusMap().size() <= 0) {
                    continue;
                }
                final List<Integer> statusList = new ArrayList<Integer>();
                for (final Map.Entry<String, Object> entry : roleObject.getStatusMap().entrySet()) {
                    if (!entry.getKey().matches("^[0-9]+$")) {
                        continue;
                    }
                    if (roleObject.getStatusMap().getInt((String)entry.getKey(), 0) <= 0) {
                        continue;
                    }
                    statusList.add(Integer.parseInt(entry.getKey()));
                    statusList.add(roleObject.getStatusMap().getInt((String)entry.getKey(), 0));
                }
                if (statusList.size() <= 0) {
                    continue;
                }
                fightRoundParam.addStatus(role.getRoleId(), statusList);
            }
        }
    }
    
    public static RespFightInParam createRespFightInParam(final boolean isSpectator, final int fightId, final int roleId, final boolean isA) {
        final RespFightInParam respFightInParam = new RespFightInParam();
        if (isSpectator) {
            respFightInParam.setType((byte)2);
        }
        respFightInParam.setRoleId(roleId);
        final FightRoundParam fightRoundParam = FightParamPool.FIGHT_ROUND_PARAM.get(fightId);
        if (fightRoundParam == null) {
            return null;
        }
        if (fightRoundParam.getExcuteFightParams() != null && fightRoundParam.getExcuteFightParams().size() > 0) {
            respFightInParam.setExcuteFightParams(fightRoundParam.getExcuteFightParams());
        }
        if (fightRoundParam.getExcuteFightParams() != null) {
            respFightInParam.setExcuteFightParams(fightRoundParam.getExcuteFightParams());
        }
        if (!isSpectator) {
            final PetFightInParam petFightInParam = fightRoundParam.getPetFightInParam(roleId);
            if (petFightInParam != null) {
                respFightInParam.addPetFightInParam(petFightInParam);
            }
            final List<SkillParam> skillParamList = fightRoundParam.getSkillParamList(roleId);
            if (skillParamList != null) {
                respFightInParam.setSkillParams(fightRoundParam.getSkillParamMap().get(roleId));
            }
            final List<RefreshPet> refreshPets = fightRoundParam.getRefreshPets(roleId);
            if (refreshPets != null) {
                respFightInParam.setPetList(refreshPets);
            }
        }
        if (isA) {
            respFightInParam.getRespCombatFriends().setList(fightRoundParam.getFightMemberA());
            respFightInParam.getRespCombatOpponets().setList(fightRoundParam.getFightMemberB());
        }
        else {
            respFightInParam.getRespCombatFriends().setList(fightRoundParam.getFightMemberB());
            respFightInParam.getRespCombatOpponets().setList(fightRoundParam.getFightMemberA());
        }
        return respFightInParam;
    }
    
    public static RespRoundStartParam getRespRoundStartParam(final int fightId, final int roleId) {
        final FightRoundParam fightRoundParam = FightParamPool.FIGHT_ROUND_PARAM.get(fightId);
        if (fightRoundParam == null) {
            return null;
        }
        return fightRoundParam.getRespRoundStartParamMap().get(roleId);
    }
    
    public static RespRoundStartParam getRespRoundStartParam(final Fight fight, final boolean isA) {
        final FightRoundParam fightRoundParam = FightParamPool.FIGHT_ROUND_PARAM.get(fight.getId());
        if (fightRoundParam == null) {
            return null;
        }
        FightObject obj = null;
        if (isA) {
            obj = FightBeforeActionUtil.getTargetForPos(fight.getListA(), 3);
        }
        else {
            obj = FightBeforeActionUtil.getTargetForPos(fight.getListB(), 3);
        }
        if (obj == null) {
            return null;
        }
        if (fightRoundParam.getRespRoundStartParamMap().size() > 0) {
            for (final Map.Entry<Integer, RespRoundStartParam> entry : fightRoundParam.getRespRoundStartParamMap().entrySet()) {
                if (obj.getId().equals(entry.getKey())) {
                    return entry.getValue();
                }
            }
        }
        return null;
    }
    
    public static FightRoundParam gainFightRoundParam(final int fightId) {
        FightRoundParam fightRoundParam = FightParamPool.FIGHT_ROUND_PARAM.get(fightId);
        if (fightRoundParam == null) {
            fightRoundParam = new FightRoundParam();
            FightParamPool.FIGHT_ROUND_PARAM.put(fightId, fightRoundParam);
            if (FightParamPool.FIGHT_ROUND_PARAM.size() > 10000) {
                EventDispatcher.getInstance().fireEvent(new FightRoundParamEvent(EventType.FIGHT_ROUND_PARAM));
            }
        }
        return fightRoundParam;
    }
    
    public static SkillRecord addSkillRecord(final Fight fight, final int type, final long attaId, final int skill) {
        FightSkillRecord fightSkillRecord = FightParamPool.FIGHT_SKILL_RECORD.get(fight.getId());
        if (fightSkillRecord == null) {
            fightSkillRecord = new FightSkillRecord();
            fightSkillRecord.setFightId(fight.getId());
            fightSkillRecord.setCreateTime(System.currentTimeMillis());
            fightSkillRecord.setRoleIds(getRoleIds(fight));
            FightParamPool.FIGHT_SKILL_RECORD.put(fight.getId(), fightSkillRecord);
        }
        final SkillRecord skillRecord = new SkillRecord();
        skillRecord.setO(fightSkillRecord.getDataList().size() + 1);
        skillRecord.setA(attaId);
        skillRecord.setP(type);
        skillRecord.setR(fight.getRound());
        skillRecord.setS(skill);
        fightSkillRecord.getDataList().add(skillRecord);
        return skillRecord;
    }
    
    private static String getRoleIds(final Fight fight) {
        List<FightObject> list;
        if (fight.isPvp() == 1) {
            list = fight.getAllFightObjects();
        }
        else {
            list = fight.getListA();
        }
        if (list != null && list.size() > 0) {
            final StringBuffer sb = new StringBuffer("[");
            for (int i = 0; i < list.size(); ++i) {
                if (list.get(i).isPlayer()) {
                    sb.append(list.get(i).getId());
                    sb.append(",");
                }
            }
            sb.deleteCharAt(sb.length() - 1);
            sb.append("]");
            return sb.toString();
        }
        return "[]";
    }
    
    public static void excuteSaveSkillRecord(final Fight fight) {
        if (fight != null && fight.getId() > 0) {
            final FightSkillRecord fightSkillRecord = FightParamPool.FIGHT_SKILL_RECORD.get(fight.getId());
            if (fightSkillRecord != null) {
                fightSkillRecord.setDestroyTime(System.currentTimeMillis());
                fightSkillRecord.save();
                FightParamPool.FIGHT_SKILL_RECORD.remove(fight.getId());
            }
            else {
                final FightSkillRecord fightSkillRecord2 = new FightSkillRecord();
                fightSkillRecord2.setFightId(fight.getId());
                fightSkillRecord2.setCreateTime(fight.getCreateTime());
                fightSkillRecord2.setRoleIds(getRoleIds(fight));
                fightSkillRecord2.setDestroyTime(System.currentTimeMillis());
                fightSkillRecord2.save();
            }
        }
    }
    
    public static void fightDestroy(final Fight fight) {
        if (fight != null && fight.getId() > 0) {
            FightParamPool.FIGHT_ROUND_PARAM.remove(fight.getId());
            excuteSaveSkillRecord(fight);
        }
    }
    
    public static void addDeathPet(final Fight fight, final int roleId, final PetFightObject petFightObject) {
        if (fight.isPvp() == 1 && petFightObject != null && petFightObject.isFallDown()) {
            final FightObjectInfo fightObjectInfo = gainInfo(fight.getId());
            List<Integer> pets = fightObjectInfo.getPetMap().get(roleId);
            if (pets == null) {
                pets = new ArrayList<Integer>();
                fightObjectInfo.getPetMap().put(roleId, pets);
            }
            pets.add(petFightObject.getId());
        }
    }
    
    public static int getDeathPetNum(final int fightId, final int roleId) {
        final FightObjectInfo fightObjectInfo = FightParamPool.FIGHTOBJECT_INFO.get(fightId);
        if (fightObjectInfo != null) {
            final List<Integer> petIds = fightObjectInfo.getPetMap().get(roleId);
            if (petIds != null) {
                return petIds.size();
            }
        }
        return 0;
    }
    
    public static void addControlStatus(final Fight fight, final FightObject target, final int status) {
        if (fight.isPvp() == 1 && StatusConst.getControlStatus().contains(status) && target.getStatusValue(status) <= 0) {
            final FightObjectInfo fightObjectInfo = gainInfo(fight.getId());
            FightObject host = target;
            if (!target.isPlayer()) {
                host = fight.queryFightObject(target.getHostId());
            }
            if (host == null) {
                return;
            }
            Map<Integer, Map<Integer, Integer>> roleMap = fightObjectInfo.getStatusNumMap().get(host.getId());
            if (roleMap == null) {
                roleMap = new HashMap<Integer, Map<Integer, Integer>>();
                fightObjectInfo.getStatusNumMap().put(host.getId(), roleMap);
            }
            Map<Integer, Integer> objMap = roleMap.get(target.getId());
            if (objMap == null) {
                objMap = new HashMap<Integer, Integer>();
                roleMap.put(target.getId(), objMap);
            }
            Integer num = objMap.get(status);
            if (num == null || num < 0) {
                num = 0;
            }
            ++num;
            objMap.put(status, num);
        }
    }
    
    public static int getControlNum(final int fightId, final int roleId) {
        final FightObjectInfo fightObjectInfo = FightParamPool.FIGHTOBJECT_INFO.get(fightId);
        if (fightObjectInfo != null) {
            int num = 0;
            final Map<Integer, Map<Integer, Integer>> roleMap = fightObjectInfo.getStatusNumMap().get(roleId);
            if (roleMap != null) {
                for (final Map.Entry<Integer, Map<Integer, Integer>> entry : roleMap.entrySet()) {
                    final Map<Integer, Integer> objMap = entry.getValue();
                    if (objMap != null) {
                        for (final Map.Entry<Integer, Integer> entry2 : objMap.entrySet()) {
                            if (entry2 != null) {
                                final Integer statusNum = entry2.getValue();
                                if (statusNum == null || !StatusConst.getControlStatus().contains(entry2.getKey())) {
                                    continue;
                                }
                                num += statusNum;
                            }
                        }
                    }
                }
            }
            return num;
        }
        return 0;
    }
    
    private static FightObjectInfo gainInfo(final int fightId) {
        FightObjectInfo fightObjectInfo = FightParamPool.FIGHTOBJECT_INFO.get(fightId);
        if (fightObjectInfo == null) {
            fightObjectInfo = new FightObjectInfo();
            FightParamPool.FIGHTOBJECT_INFO.put(fightId, fightObjectInfo);
        }
        return fightObjectInfo;
    }
    
    static {
        FightParamPool.FIGHT_ROUND_PARAM = new ConcurrentHashMap<Integer, FightRoundParam>();
        FightParamPool.FIGHT_SKILL_RECORD = new ConcurrentHashMap<Integer, FightSkillRecord>();
        FightParamPool.FIGHTOBJECT_INFO = new ConcurrentHashMap<Integer, FightObjectInfo>();
    }
}
