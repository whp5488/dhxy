package com.kitty.system.game.fightnew.spectator.pool;

import java.util.HashMap;
import com.kitty.system.game.vo.utils.StatusConst;
import com.kitty.system.game.fightnew.bean.PetFightObject;
import com.kitty.system.game.fightRecord.domain.SkillRecord;
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
    
    public FightParamPool() {
    }
    
    public static void addFightInParam(Fight fight) {
        FightNewService fightNewService = (FightNewService)SpringUtils.getBean(FightNewService.class);
        FightRoundParam fightRoundParam = gainFightRoundParam(fight.getId());
        fightRoundParam.setFightMemberA(fightNewService.getFightMemberList(fight.getListA()));
        fightRoundParam.setFightMemberB(fightNewService.getFightMemberList(fight.getListB()));
        List<ExcuteFightParam> excuteFightParams = fight.getFightStatus();
        gainFightRoundParam(fight.getId()).setExcuteFightParams(excuteFightParams);
        for (FightObject roleObject : fight.getAllFightObjects()) {
            if (roleObject.isPlayer()) {
                Role role = roleObject.getRole();
                FightObject petObject = fight.queryFightObject(Integer.valueOf(roleObject.getFightPetId()));
                if (petObject != null) {
                    Pet pet = petObject.getPet();
                    if (pet != null) {
                        PetFightInParam petFightParam = fight.getPetFightParam(pet);
                        fightRoundParam.addPetFightInParam((int)role.getRoleId(), petFightParam);
                    }
                }
                List<SkillParam> skillParams = fight.getSkillParams(role);
                if (skillParams != null) {
                    fightRoundParam.addSkillParam((int)role.getRoleId(), skillParams);
                }
                List<RefreshPet> refreshPets = fightNewService.getFightInParamPetList(fight, roleObject);
                fightRoundParam.addRefreshPet((int)role.getRoleId(), refreshPets);
                if (roleObject.getStatusMap().size() > 0) {
                    List<Integer> statusList = new ArrayList<>();
                    for (Map.Entry<String, Object> entry : roleObject.getStatusMap().entrySet()) {
                        if (!((String)entry.getKey()).matches("^[0-9]+$")) {
                            continue;
                        }
                        if (roleObject.getStatusMap().getInt((String)entry.getKey(), 0) > 0) {
                            statusList.add(Integer.valueOf(Integer.parseInt((String)entry.getKey())));
                            statusList.add(Integer.valueOf(roleObject.getStatusMap().getInt((String)entry.getKey(), 0)));
                        }
                        else {
                            continue;
                        }
                    }
                    if (statusList.size() > 0) {
                        fightRoundParam.addStatus((int)role.getRoleId(), statusList);
                    }
                    else {
                        continue;
                    }
                }
                else {
                    continue;
                }
            }
            else {
                continue;
            }
        }
    }
    
    public static RespFightInParam createRespFightInParam(boolean isSpectator, int fightId, int roleId, boolean isA) {
        RespFightInParam respFightInParam = new RespFightInParam();
        if (isSpectator) {
            respFightInParam.setType((byte)2);
        }
        respFightInParam.setRoleId(roleId);
        FightRoundParam fightRoundParam = (FightRoundParam)FightParamPool.FIGHT_ROUND_PARAM.get(Integer.valueOf(fightId));
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
            PetFightInParam petFightInParam = fightRoundParam.getPetFightInParam(roleId);
            if (petFightInParam != null) {
                respFightInParam.addPetFightInParam(petFightInParam);
            }
            List<SkillParam> skillParamList = fightRoundParam.getSkillParamList(roleId);
            if (skillParamList != null) {
                respFightInParam.setSkillParams((List)fightRoundParam.getSkillParamMap().get(Integer.valueOf(roleId)));
            }
            List<RefreshPet> refreshPets = fightRoundParam.getRefreshPets(roleId);
            if (refreshPets != null) {
                respFightInParam.setPetList(refreshPets);
            }
        }
        if (isA) {
            respFightInParam.getRespCombatFriends().setList(fightRoundParam.getFightMemberA());
            respFightInParam.getRespCombatOpponets().setList(fightRoundParam.getFightMemberB());
            return respFightInParam;
        }
        respFightInParam.getRespCombatFriends().setList(fightRoundParam.getFightMemberB());
        respFightInParam.getRespCombatOpponets().setList(fightRoundParam.getFightMemberA());
        return respFightInParam;
    }
    
    public static RespRoundStartParam getRespRoundStartParam(int fightId, int roleId) {
        FightRoundParam fightRoundParam = (FightRoundParam)FightParamPool.FIGHT_ROUND_PARAM.get(Integer.valueOf(fightId));
        if (fightRoundParam == null) {
            return null;
        }
        return (RespRoundStartParam)fightRoundParam.getRespRoundStartParamMap().get(Integer.valueOf(roleId));
    }
    
    public static RespRoundStartParam getRespRoundStartParam(Fight fight, boolean isA) {
        FightRoundParam fightRoundParam = (FightRoundParam)FightParamPool.FIGHT_ROUND_PARAM.get(Integer.valueOf(fight.getId()));
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
            for (Map.Entry<Integer, RespRoundStartParam> entry : fightRoundParam.getRespRoundStartParamMap().entrySet()) {
                if (obj.getId().equals(entry.getKey())) {
                    return (RespRoundStartParam)entry.getValue();
                }
                continue;
            }
        }
        return null;
    }
    
    public static FightRoundParam gainFightRoundParam(int fightId) {
        FightRoundParam fightRoundParam = (FightRoundParam)FightParamPool.FIGHT_ROUND_PARAM.get(Integer.valueOf(fightId));
        if (fightRoundParam == null) {
            fightRoundParam = new FightRoundParam();
            FightParamPool.FIGHT_ROUND_PARAM.put(Integer.valueOf(fightId), fightRoundParam);
            if (FightParamPool.FIGHT_ROUND_PARAM.size() > 10000) {
                EventDispatcher.getInstance().fireEvent(new FightRoundParamEvent(EventType.FIGHT_ROUND_PARAM));
            }
        }
        return fightRoundParam;
    }
    
    public static SkillRecord addSkillRecord(Fight fight, int type, long attaId, int skill) {
        FightSkillRecord fightSkillRecord = (FightSkillRecord)FightParamPool.FIGHT_SKILL_RECORD.get(Integer.valueOf(fight.getId()));
        if (fightSkillRecord == null) {
            fightSkillRecord = new FightSkillRecord();
            fightSkillRecord.setFightId((long)fight.getId());
            fightSkillRecord.setCreateTime(System.currentTimeMillis());
            fightSkillRecord.setRoleIds(getRoleIds(fight));
            FightParamPool.FIGHT_SKILL_RECORD.put(Integer.valueOf(fight.getId()), fightSkillRecord);
        }
        SkillRecord skillRecord = new SkillRecord();
        skillRecord.setO(fightSkillRecord.getDataList().size() + 1);
        skillRecord.setA(attaId);
        skillRecord.setP(type);
        skillRecord.setR(fight.getRound());
        skillRecord.setS(skill);
        fightSkillRecord.getDataList().add(skillRecord);
        return skillRecord;
    }
    
    private static String getRoleIds(Fight fight) {
        List<FightObject> list;
        if (fight.isPvp() == 1) {
            list = fight.getAllFightObjects();
        }
        else {
            list = fight.getListA();
        }
        if (list != null && list.size() > 0) {
            StringBuffer sb = new StringBuffer("[");
            for (int i = 0; i < list.size(); ++i) {
                if (((FightObject)list.get(i)).isPlayer()) {
                    sb.append(((FightObject)list.get(i)).getId());
                    sb.append(",");
                }
            }
            sb.deleteCharAt(sb.length() - 1);
            sb.append("]");
            return sb.toString();
        }
        return "[]";
    }
    
    public static void excuteSaveSkillRecord(Fight fight) {
        if (fight != null && fight.getId() > 0) {
            FightSkillRecord fightSkillRecord = (FightSkillRecord)FightParamPool.FIGHT_SKILL_RECORD.get(Integer.valueOf(fight.getId()));
            if (fightSkillRecord != null) {
                fightSkillRecord.setDestroyTime(System.currentTimeMillis());
                fightSkillRecord.save();
                FightParamPool.FIGHT_SKILL_RECORD.remove(Integer.valueOf(fight.getId()));
            }
            else {
                FightSkillRecord fightSkillRecord2 = new FightSkillRecord();
                fightSkillRecord2.setFightId((long)fight.getId());
                fightSkillRecord2.setCreateTime(fight.getCreateTime());
                fightSkillRecord2.setRoleIds(getRoleIds(fight));
                fightSkillRecord2.setDestroyTime(System.currentTimeMillis());
                fightSkillRecord2.save();
            }
        }
    }
    
    public static void fightDestroy(Fight fight) {
        if (fight != null && fight.getId() > 0) {
            FightParamPool.FIGHT_ROUND_PARAM.remove(Integer.valueOf(fight.getId()));
            excuteSaveSkillRecord(fight);
        }
    }
    
    public static void addDeathPet(Fight fight, int roleId, PetFightObject petFightObject) {
        if (fight.isPvp() == 1 && petFightObject != null && petFightObject.isFallDown()) {
            FightObjectInfo fightObjectInfo = gainInfo(fight.getId());
            List<Integer> pets = (List)fightObjectInfo.getPetMap().get(Integer.valueOf(roleId));
            if (pets == null) {
                pets = new ArrayList<>();
                fightObjectInfo.getPetMap().put(Integer.valueOf(roleId), pets);
            }
            pets.add(petFightObject.getId());
        }
    }
    
    public static int getDeathPetNum(int fightId, int roleId) {
        FightObjectInfo fightObjectInfo = (FightObjectInfo)FightParamPool.FIGHTOBJECT_INFO.get(Integer.valueOf(fightId));
        if (fightObjectInfo != null) {
            List<Integer> petIds = (List)fightObjectInfo.getPetMap().get(Integer.valueOf(roleId));
            if (petIds != null) {
                return petIds.size();
            }
        }
        return 0;
    }
    
    public static void addControlStatus(Fight fight, FightObject target, int status) {
        if (fight.isPvp() == 1 && StatusConst.getControlStatus().contains(Integer.valueOf(status)) && target.getStatusValue(status) <= 0) {
            FightObjectInfo fightObjectInfo = gainInfo(fight.getId());
            FightObject host = target;
            if (!target.isPlayer()) {
                host = fight.queryFightObject(Integer.valueOf(target.getHostId()));
            }
            if (host == null) {
                return;
            }
            Map<Integer, Map<Integer, Integer>> roleMap = (Map)fightObjectInfo.getStatusNumMap().get(host.getId());
            if (roleMap == null) {
                roleMap = new HashMap();
                fightObjectInfo.getStatusNumMap().put(host.getId(), roleMap);
            }
            Map<Integer, Integer> objMap = (Map)roleMap.get(target.getId());
            if (objMap == null) {
                objMap = new HashMap();
                roleMap.put(target.getId(), objMap);
            }
            Integer num = (Integer)objMap.get(Integer.valueOf(status));
            if (num == null || (int)num < 0) {
                num = Integer.valueOf(0);
            }
            num = Integer.valueOf((int)num + 1);
            objMap.put(Integer.valueOf(status), num);
        }
    }
    
    public static int getControlNum(int fightId, int roleId) {
        FightObjectInfo fightObjectInfo = (FightObjectInfo)FightParamPool.FIGHTOBJECT_INFO.get(Integer.valueOf(fightId));
        if (fightObjectInfo != null) {
            int num = 0;
            Map<Integer, Map<Integer, Integer>> roleMap = (Map)fightObjectInfo.getStatusNumMap().get(Integer.valueOf(roleId));
            if (roleMap != null) {
                for (Map.Entry<Integer, Map<Integer, Integer>> entry : roleMap.entrySet()) {
                    Map<Integer, Integer> objMap = (Map)entry.getValue();
                    if (objMap != null) {
                        for (Map.Entry<Integer, Integer> entry2 : objMap.entrySet()) {
                            if (entry2 != null) {
                                Integer statusNum = (Integer)entry2.getValue();
                                if (statusNum != null && StatusConst.getControlStatus().contains(entry2.getKey())) {
                                    num += (int)statusNum;
                                }
                                else {
                                    continue;
                                }
                            }
                            else {
                                continue;
                            }
                        }
                    }
                    else {
                        continue;
                    }
                }
            }
            return num;
        }
        else {
            return 0;
        }
    }
    
    private static FightObjectInfo gainInfo(int fightId) {
        FightObjectInfo fightObjectInfo = (FightObjectInfo)FightParamPool.FIGHTOBJECT_INFO.get(Integer.valueOf(fightId));
        if (fightObjectInfo == null) {
            fightObjectInfo = new FightObjectInfo();
            FightParamPool.FIGHTOBJECT_INFO.put(Integer.valueOf(fightId), fightObjectInfo);
            return fightObjectInfo;
        }
        return fightObjectInfo;
    }
    
    static {
        FightParamPool.FIGHT_ROUND_PARAM = new ConcurrentHashMap();
        FightParamPool.FIGHT_SKILL_RECORD = new ConcurrentHashMap();
        FightParamPool.FIGHTOBJECT_INFO = new ConcurrentHashMap();
    }
}
