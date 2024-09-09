package com.kitty.system.game.fightnew.service;

import com.kitty.system.enums.war.itempro.def.PET_PASSIVE_SKILL_TRIGGER_DICT;
import java.util.Map;
import com.kitty.system.game.fightnew.imbue.utils.ImbueUtil;
import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.system.game.fightnew.book.pet.utils.PetTriggerPassiveSkill;
import java.util.Arrays;
import com.kitty.system.init.domain.SysSkill;
import com.kitty.system.enums.war.warskill.skilldef.PETSKILLATTR;
import com.kitty.system.enums.war.warskill.skilldef.SKILLATTR;
import com.kitty.system.game.skill.pool.SkillDataPool;
import com.kitty.system.init.domain.SysPetSkill;
import com.kitty.system.game.skill.domain.RoleSkill;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.book.pet.PetPassiveSkillUtil;
import com.kitty.common.utils.BigDecimalUtil;
import com.kitty.system.init.pool.ItemDataPool;
import com.kitty.system.init.domain.SysDrug;
import com.kitty.system.game.fightnew.book.monster.MonsterSkillUtils;
import com.kitty.common.utils.bean.BeanUtils;
import com.kitty.system.game.fightnew.bean.MonsterFightObject;
import java.util.Comparator;
import java.util.Random;
import java.util.ArrayList;
import com.kitty.system.game.vo.utils.Const;
import com.kitty.system.game.fightnew.utils.SkillAi;
import com.kitty.system.init.domain.vo.MonsterParameter;
import com.kitty.system.game.role.domain.Role;
import java.util.concurrent.ConcurrentHashMap;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.enums.war.wardef.LOGIC;
import com.kitty.system.game.fightnew.messages.old.RespEndCombat;
import java.util.Iterator;
import java.util.stream.Collectors;
import com.kitty.system.enums.war.wardef.RACE;
import java.util.List;
import com.kitty.system.game.fightnew.pool.FightDataPool;
import com.kitty.system.init.domain.SysDataMonster;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.logs.LoggerFunction;
import com.kitty.system.game.broadcast.BroadcastService;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class FightMonsterNewService
{
    private final Logger logger;
    @Autowired
    public FightNewService fightNewService;
    @Autowired
    public BroadcastService broadcastService;
    
    public FightMonsterNewService() {
        this.logger = LoggerFunction.FIGHT.getLogger();
    }
    
    public boolean runAway(Fight fight, FightObject attacker) {
        SysDataMonster monster = (SysDataMonster)FightDataPool.data_monster.get(Integer.valueOf(attacker.getSerial()));
        if (monster == null) {
            return false;
        }
        if ((int)monster.getEscapeRound() == -1 || fight.getRound() < (int)monster.getEscapeRound()) {
            return false;
        }
        if (attacker.getStatusValue(1002) > 0 && this.isRunAway(monster.getCEscapeRatio())) {
            return true;
        }
        Iterator<FightObject> iterator = fight.getListA().iterator();
        while (iterator.hasNext()) {
        	FightObject obj = (FightObject)iterator.next();
            if (1 == obj.getAutoFight() && this.isRunAway(monster.getCEscapeRatio())) {
                return true;
            }
            continue;
        }
        if (attacker.getStatusValue(1005) > 0 && this.isRunAway(monster.getEscapeRatio())) {
            return true;
        }
        List<FightObject> targetSide = (List)fight.getTargetSide((int)monster.getId()).stream().filter(obj -> !obj.isFallDown() && (RACE.RACE_REN.getValue() == obj.getRace() || RACE.RACE_GUI.getValue() == obj.getRace())).collect(Collectors.toList());
        if (targetSide.size() > 0 && attacker.getStatusValue(1003) <= 0 && this.isRunAway(monster.getEscapeRatio())) {
            return true;
        }
        List<FightObject> mySide = (List)fight.getMySide((int)monster.getId()).stream().filter(obj -> !obj.isFallDown()).collect(Collectors.toList());
        boolean isFlag = true;
        for (FightObject fightObject : mySide) {
            SysDataMonster mySideMonster = (SysDataMonster)FightDataPool.data_monster.get(Integer.valueOf(fightObject.getSerial()));
            if ((int)mySideMonster.getIsBoss() == 0) {
                isFlag = false;
                break;
            }
            continue;
        }
        return isFlag && attacker.getStatusValue(1003) > 0 && this.isRunAway(monster.getEscapeRatio());
    }
    
    public void monsterFlee(FightObject fightObject, Fight fight) {
        ConcurrentHashMap<Integer, Integer> fightRooms = FightExecutorNewService.FIGHT_ROOMS;
        fight.getListB().remove(fightObject);
        if (fight.getListB() == null || fight.getListB().size() <= 0) {
            RespEndCombat respEndCombat = new RespEndCombat((short)2);
            for (FightObject obj : fight.getListA()) {
                if (obj.getLogic() == LOGIC.LOGICTYPE_HERO.getValue()) {
                    Role role = obj.getRole();
                    MessagePusher.pushMessage(role, respEndCombat);
                }
                else {
                    continue;
                }
            }
        }
    }
    
    public boolean isSelectMonster(Fight fight, FightObject attacker) {
        if (!attacker.isMonster()) {
            return false;
        }
        List<FightObject> mySides = (List)fight.getMySide((int)attacker.getId()).stream().filter(obj -> !obj.isFallDown()).collect(Collectors.toList());
        if (mySides.size() >= 10) {
            return false;
        }
        SysDataMonster sysDataMonster = (SysDataMonster)FightDataPool.data_monster.get(Integer.valueOf(attacker.getSerial()));
        if (sysDataMonster == null) {
            return false;
        }
        List<MonsterParameter> selectParameterList = sysDataMonster.getSelectParameterList();
        if (selectParameterList == null || selectParameterList.size() <= 0) {
            return false;
        }
        int timeForFight = 0;
        for (MonsterParameter parameter : selectParameterList) {
            if (fight.getRound() > parameter.getRound()) {
                timeForFight = fight.getMonsterAstrict().getInt(parameter.getType());
                boolean flag = SkillAi.canAffectOnPro(parameter.getPercent());
                if (flag) {
                    ++timeForFight;
                    fight.getMonsterAstrict().setv(parameter.getType(), Integer.valueOf(timeForFight));
                }
                return flag;
            }
            else {
                continue;
            }
        }
        return false;
    }
    
    public void selectMonster(Fight fight, FightObject attacker) {
        if (this.fightNewService.checkDeStatus(fight, attacker)) {
            return;
        }
        List<FightObject> targetSide = (List)fight.getTargetSide((int)attacker.getId()).stream().filter(obj -> !obj.isFallDown()).collect(Collectors.toList());
        if (targetSide == null || targetSide.size() <= 0) {
            return;
        }
        FightObject targetObject = (FightObject)targetSide.get(0);
        Role role = null;
        if (targetObject.isPlayer()) {
            role = targetObject.getRole();
        }
        else {
            FightObject targetHostObject = fight.queryFightObject(Integer.valueOf(targetObject.getHostId()));
            if (targetHostObject != null) {
                role = targetHostObject.getRole();
            }
        }
        if (role == null) {
            return;
        }
        attacker.recordAction(fight);
        if (this.selectForStar18(fight, attacker)) {
            return;
        }
        int num = 1;
        List<FightObject> mySides = (List)fight.getMySide((int)attacker.getId()).stream().filter(obj -> !obj.isFallDown()).collect(Collectors.toList());
        List<Integer> possStatic = Const.poss;
        List<Integer> poss = new ArrayList<>();
        poss.addAll(possStatic);
        for (FightObject fightObject : mySides) {
            for (int i = poss.size() - 1; i >= 0; --i) {
                if (fightObject.getPos() == (int)poss.get(i)) {
                    poss.remove(poss.get(i));
                }
            }
        }
        if (num > poss.size()) {
            num = poss.size();
        }
        if (num <= 0) {
            attacker.setType(0);
            attacker.setSkillId(0);
            this.fightNewService.normalAttack(attacker, fight);
            return;
        }
        Random random = new Random();
        List<FightObject> monsterList = (List)fight.getListB().stream().sorted(Comparator.comparing(FightObject::getId).reversed()).collect(Collectors.toList());
        int monsterId = (int)((FightObject)monsterList.get(0)).getId() + 1;
        for (int j = 0; j < num; ++j) {
            int posIndex = random.nextInt(poss.size());
            Integer selectMonsterSerial = ((MonsterFightObject)attacker).getSelectMonsterSerial();
            if ((int)selectMonsterSerial != 0) {
                SysDataMonster dataMonster = (SysDataMonster)FightDataPool.data_monster.get(selectMonsterSerial);
                SysDataMonster monster = new SysDataMonster();
                BeanUtils.copyBeanProp(monster, dataMonster);
                monster.setPos((int)poss.get(posIndex));
                poss.remove(posIndex);
                MonsterFightObject fightObject2 = new MonsterFightObject(monster, Integer.valueOf(monster.getPos()), monsterId);
                attacker.setTargetId((int)fightObject2.getId());
                MonsterSkillUtils.star18PetAppear(fight, attacker, fightObject2, monster.getPos(), false);
                ++monsterId;
            }
        }
        return;
    }
    
    private boolean selectForStar18(Fight fight, FightObject attacker) {
        if (fight.getSpecialMonsterType() == 1 && attacker.getPos() == 10003) {
            SysDataMonster petMonster = MonsterSkillUtils.getStar18SelectPet();
            if (petMonster != null) {
                MonsterSkillUtils.randomMonsterPetSkill(petMonster);
                petMonster.setPos(attacker.getPos() + 100);
                List<FightObject> monsterList = (List)fight.getListB().stream().sorted(Comparator.comparing(FightObject::getId).reversed()).collect(Collectors.toList());
                int monsterId = (int)((FightObject)monsterList.get(0)).getId() + 1;
                MonsterFightObject fightObject = new MonsterFightObject(petMonster, Integer.valueOf(petMonster.getPos()), monsterId);
                MonsterSkillUtils.star18PetAppear(fight, attacker, fightObject, fightObject.getPos(), true);
            }
        }
        return false;
    }
    
    public boolean isUseItem(Fight fight, FightObject attacker) {
        double residueLife = 0.2;
        double residueMana = 0.2;
        SysDataMonster sysDataMonster = (SysDataMonster)FightDataPool.data_monster.get(Integer.valueOf(attacker.getSerial()));
        if (sysDataMonster == null) {
            return false;
        }
        List<MonsterParameter> itemUseParameterList = sysDataMonster.getItemUseParameterList();
        if (itemUseParameterList == null || itemUseParameterList.size() <= 0) {
            return false;
        }
        SysDrug sysDrug = null;
        List<FightObject> mySide = fight.getMySide((int)attacker.getId());
        List<FightObject> targetList = null;
        FightObject target = null;
        for (MonsterParameter parameter : itemUseParameterList) {
            if (fight.getRound() <= parameter.getRound()) {
                continue;
            }
            boolean flag = SkillAi.canAffectOnPro(parameter.getPercent());
            if (!flag) {
                continue;
            }
            sysDrug = (SysDrug)ItemDataPool.data_drug.get(parameter.getSerial());
            if (sysDrug == null) {
                continue;
            }
            switch ((int)sysDrug.getShowClass()) {
                case 1: {
                    targetList = (List)mySide.stream().filter(obj -> obj.getSurplusLifePercent() < residueLife).sorted(Comparator.comparing(FightObject::getSurplusLifePercent)).collect(Collectors.toList());
                    if (targetList.size() <= 0) {
                        continue;
                    }
                    target = (FightObject)targetList.get(0);
                    break;
                }
                case 2: {
                    targetList = (List)mySide.stream().filter(obj -> obj.getSurplusManaPercent() < residueMana).sorted(Comparator.comparing(FightObject::getSurplusManaPercent)).collect(Collectors.toList());
                    if (targetList.size() <= 0) {
                        continue;
                    }
                    target = (FightObject)targetList.get(0);
                    break;
                }
                case 3: {
                    targetList = (List)mySide.stream().filter(obj -> Double.parseDouble(BigDecimalUtil.divide(String.valueOf(obj.getCurrLife()), String.valueOf(obj.getMaxLife()))) < residueLife).sorted(Comparator.comparing(FightObject::getSpeed).reversed()).collect(Collectors.toList());
                    if (targetList.size() <= 0) {
                        continue;
                    }
                    target = (FightObject)targetList.get(0);
                    break;
                }
                default: {
                    if (sysDrug.getSerial().equals(Integer.valueOf(10022))) {
                        targetList = (List)fight.getMySide((int)attacker.getId()).stream().filter(obj -> !obj.isFallDown() && PetPassiveSkillUtil.isAbnormalOrPositiveStatus(obj.getStatusMap(), "abnormal")).sorted(Comparator.comparing(FightObject::getSpeed).reversed()).collect(Collectors.toList());
                        if (targetList.size() <= 0) {
                            continue;
                        }
                        target = (FightObject)targetList.get(0);
                    }
                    if (sysDrug.getSerial().equals(Integer.valueOf(10024))) {
                        Iterator<FightObject> iterator2 = mySide.iterator();
                        while (iterator2.hasNext()) {
                        	FightObject obj = (FightObject)iterator2.next();
                            NutMap fightActionRecord = obj.getFightActionRecord();
                            if (fightActionRecord.getInt(fight.getRound() + "details", 0) == 10024) {
                                return false;
                            }
                            continue;
                        }
                        targetList = (List)fight.getTargetSide((int)attacker.getId()).stream().filter(obj -> obj.getStatusValue(1011) > 0).sorted(Comparator.comparing(FightObject::getSpeed).reversed()).collect(Collectors.toList());
                        if (targetList.size() <= 0) {
                            continue;
                        }
                        target = (FightObject)targetList.get(0);
                        break;
                    }
                    else {
                        break;
                    }
                }
            }
            attacker.setSkillId((int)sysDrug.getSerial());
            attacker.setTargetId((int)target.getId());
            return true;
        }
        return false;
    }
    
    public boolean isUseSkills(Fight fight, FightObject attacker) {
        SysDataMonster sysDataMonster = (SysDataMonster)FightDataPool.data_monster.get(Integer.valueOf(attacker.getSerial()));
        if (sysDataMonster == null) {
            List<RoleSkill> skills = (List)attacker.getSkillIds().stream().filter(obj -> (int)obj.getSkillId() > 0).collect(Collectors.toList());
            if (skills.size() > 0) {
                for (RoleSkill skill : skills) {
                    SysPetSkill sysPetSkill = (SysPetSkill)SkillDataPool.petSkillMap.get(skill.getSkillId());
                    if (sysPetSkill == null) {
                        continue;
                    }
                    if ((int)sysPetSkill.getInitiative() > 0 && sysPetSkill.getSerial().equals(skill.getSkillId())) {
                        attacker.setSkillId((int)skill.getSkillId());
                        return true;
                    }
                    continue;
                }
            }
            return false;
        }
        else {
            double fsGjGl = sysDataMonster.getFsGjGl();
            boolean flag = SkillAi.canAffectOnPro(fsGjGl);
            if (!flag) {
                return false;
            }
            List<RoleSkill> skillIds = (List)attacker.getSkillIds().stream().filter(skill -> (int)skill.getSkillId() > 0 && (String.valueOf(skill.getSkillId()).length() == 5 || String.valueOf(skill.getSkillId()).length() == 6)).collect(Collectors.toList());
            boolean isUseSkill = false;
            if (skillIds.size() > 0) {
                if (this.checkCure(fight, attacker, skillIds) || this.checkAbnormal(fight, attacker, skillIds) || this.checkGain(fight, attacker, skillIds) || this.checkObstacle(fight, attacker, skillIds) || this.checkPositive(fight, attacker, skillIds) || this.checkDebuffs(fight, attacker, skillIds) || this.checkClearWuXing(fight, attacker, skillIds)) {
                    isUseSkill = true;
                }
                else {
                    List<RoleSkill> roleSkills = (List)skillIds.stream().filter(skill -> this.includeSkill(skill.getSkillId())).collect(Collectors.toList());
                    List<RoleSkill> roleInitiativeSkills = this.excludePassiveSkills(roleSkills);
                    if (roleInitiativeSkills != null && roleInitiativeSkills.size() > 0) {
                        Random rd = new Random();
                        int i = rd.nextInt(roleInitiativeSkills.size());
                        attacker.setSkillId((int)((RoleSkill)roleInitiativeSkills.get(i)).getSkillId());
                        isUseSkill = true;
                    }
                }
            }
            return isUseSkill;
        }
    }
    
    private boolean includeSkill(Integer skillId) {
        if (39006 == (int)skillId) {
            return true;
        }
        List<Integer> SKILLATTRS = new ArrayList<Integer>() {
            {
                this.add(Integer.valueOf(SKILLATTR.SKILLATTR_ZHIYU.getValue()));
                this.add(Integer.valueOf(SKILLATTR.SKILLATTR_PAN.getValue()));
                this.add(Integer.valueOf(SKILLATTR.SKILLATTR_ATTACK.getValue()));
                this.add(Integer.valueOf(SKILLATTR.SKILLATTR_SPEED.getValue()));
                this.add(Integer.valueOf(SKILLATTR.SKILLATTR_SLEEP.getValue()));
                this.add(Integer.valueOf(SKILLATTR.SKILLATTR_CONFUSE.getValue()));
                this.add(Integer.valueOf(SKILLATTR.SKILLATTR_ICE.getValue()));
                this.add(Integer.valueOf(SKILLATTR.SKILLATTR_YIWANG.getValue()));
                this.add(Integer.valueOf(SKILLATTR.SKILLATTR_SHUAIRUO.getValue()));
            }
        };
        List<Integer> PETSKILLATTRS = new ArrayList<Integer>() {
            {
                this.add(Integer.valueOf(PETSKILLATTR.PETSKILLATTR__ZHIYU.getValue()));
                this.add(Integer.valueOf(PETSKILLATTR.PETSKILLATTR_RELIEVE_ABNORMAL.getValue()));
                this.add(Integer.valueOf(PETSKILLATTR.PETSKILLATTR_GAIN.getValue()));
                this.add(Integer.valueOf(PETSKILLATTR.PETSKILLATTR_RELIEVE_POSITIVE.getValue()));
                this.add(Integer.valueOf(PETSKILLATTR.PETSKILLATTR_DEBUFFS.getValue()));
                this.add(Integer.valueOf(PETSKILLATTR.PETSKILLATTR_RELIEVE_WX.getValue()));
            }
        };
        String indexNumStr = skillId.toString().substring(0, 1);
        if ("3".equals(indexNumStr)) {
            SysSkill sysSkill = (SysSkill)SkillDataPool.skillMap.get(skillId);
            if (sysSkill == null) {
                return false;
            }
            if (SKILLATTRS.contains(sysSkill.getAttr())) {
                return false;
            }
        }
        else if ("8".equals(indexNumStr)) {
            SysPetSkill petSkill = (SysPetSkill)SkillDataPool.petSkillMap.get(skillId);
            if (petSkill == null) {
                return false;
            }
            if (PETSKILLATTRS.contains(petSkill.getAttr())) {
                return false;
            }
        }
        return true;
    }
    
    private boolean checkCure(Fight fight, FightObject attacker, List<RoleSkill> skillIds) {
        List<FightObject> mySide = fight.getMySide((int)attacker.getId());
        List<FightObject> targetList = (List)mySide.stream().filter(obj -> !obj.isFallDown() && Double.parseDouble(BigDecimalUtil.divide(String.valueOf(obj.getCurrLife()), String.valueOf(obj.getMaxLife()))) < 0.2).collect(Collectors.toList());
        if (targetList.size() > 0) {
            boolean flag = this.getFlag(attacker, skillIds, Arrays.asList(Integer.valueOf(SKILLATTR.SKILLATTR_ZHIYU.getValue())), Arrays.asList(Integer.valueOf(PETSKILLATTR.PETSKILLATTR__ZHIYU.getValue())));
            if (flag) {
                targetList = (List)targetList.stream().sorted(Comparator.comparing(FightObject::getSpeed).reversed()).collect(Collectors.toList());
                ((MonsterFightObject)attacker).setPredictTargetList(targetList);
                return true;
            }
        }
        return false;
    }
    
    private boolean checkAbnormal(Fight fight, FightObject attacker, List<RoleSkill> skillIds) {
        List<FightObject> abnormalTargetList = (List)fight.getMySide((int)attacker.getId()).stream().filter(obj -> !obj.isFallDown() && PetPassiveSkillUtil.isAbnormalOrPositiveStatus(obj.getStatusMap(), "abnormal")).collect(Collectors.toList());
        if (abnormalTargetList.size() > 0) {
            boolean flag = this.getFlag(attacker, skillIds, null, Arrays.asList(Integer.valueOf(PETSKILLATTR.PETSKILLATTR_RELIEVE_ABNORMAL.getValue())));
            if (flag) {
                abnormalTargetList = (List)abnormalTargetList.stream().sorted(Comparator.comparing(FightObject::getSpeed).reversed()).collect(Collectors.toList());
                ((MonsterFightObject)attacker).setPredictTargetList(abnormalTargetList);
                return true;
            }
        }
        return false;
    }
    
    private boolean checkGain(Fight fight, FightObject attacker, List<RoleSkill> skillIds) {
        boolean flag = this.getFlag(attacker, skillIds, Arrays.asList(Integer.valueOf(SKILLATTR.SKILLATTR_PAN.getValue()), Integer.valueOf(SKILLATTR.SKILLATTR_ATTACK.getValue()), Integer.valueOf(SKILLATTR.SKILLATTR_SPEED.getValue())), Arrays.asList(Integer.valueOf(PETSKILLATTR.PETSKILLATTR_GAIN.getValue()), Integer.valueOf(PETSKILLATTR.PETSKILLATTR_SPEED.getValue())));
        if (flag) {
            List<FightObject> gainTargetList = (List)fight.getMySide((int)attacker.getId()).stream().filter(obj -> !obj.isFallDown()).sorted(Comparator.comparing(FightObject::getSpeed).reversed()).collect(Collectors.toList());
            for (FightObject object : gainTargetList) {
                if (object.getStatusValue(1009) > 0) {
                    return false;
                }
                if (object.getStatusValue(1008) > 0) {
                    return false;
                }
                if (object.getStatusValue(1010) > 0) {
                    return false;
                }
                continue;
            }
            ((MonsterFightObject)attacker).setPredictTargetList(gainTargetList);
            return true;
        }
        return false;
    }
    
    private boolean checkObstacle(Fight fight, FightObject attacker, List<RoleSkill> skillIds) {
        boolean flag = this.getFlag(attacker, skillIds, Arrays.asList(Integer.valueOf(SKILLATTR.SKILLATTR_SLEEP.getValue()), Integer.valueOf(SKILLATTR.SKILLATTR_CONFUSE.getValue()), Integer.valueOf(SKILLATTR.SKILLATTR_ICE.getValue()), Integer.valueOf(SKILLATTR.SKILLATTR_YIWANG.getValue())), null);
        if (flag) {
            List<FightObject> obstacleTargetList = (List)fight.getTargetSide((int)attacker.getId()).stream().filter(obj -> !obj.isFallDown() && PetTriggerPassiveSkill.isMagicAttack(attacker, obj)).sorted(Comparator.comparing(FightObject::getSpeed).reversed()).collect(Collectors.toList());
            for (FightObject object : obstacleTargetList) {
                if (object.getStatusValue(1004) > 0) {
                    return false;
                }
                if (object.getStatusValue(1002) > 0) {
                    return false;
                }
                if (object.getStatusValue(1003) > 0) {
                    return false;
                }
                if (object.getStatusValue(1006) > 0) {
                    return false;
                }
                continue;
            }
            ((MonsterFightObject)attacker).setPredictTargetList(obstacleTargetList);
            return true;
        }
        return false;
    }
    
    private boolean checkPositive(Fight fight, FightObject attacker, List<RoleSkill> skillIds) {
        List<FightObject> positiveTargetList = (List)fight.getTargetSide((int)attacker.getId()).stream().filter(obj -> !obj.isFallDown() && PetTriggerPassiveSkill.isMagicAttack(attacker, obj) && PetPassiveSkillUtil.isAbnormalOrPositiveStatus(obj.getStatusMap(), "positive")).collect(Collectors.toList());
        if (positiveTargetList.size() > 0) {
            boolean flag = this.getFlag(attacker, skillIds, null, Arrays.asList(Integer.valueOf(PETSKILLATTR.PETSKILLATTR_RELIEVE_POSITIVE.getValue())));
            if (flag) {
                positiveTargetList = (List)positiveTargetList.stream().sorted(Comparator.comparing(FightObject::getSpeed).reversed()).collect(Collectors.toList());
                ((MonsterFightObject)attacker).setPredictTargetList(positiveTargetList);
                return true;
            }
        }
        return false;
    }
    
    private boolean checkDebuffs(Fight fight, FightObject attacker, List<RoleSkill> skillIds) {
        boolean flag = this.getFlag(attacker, skillIds, Arrays.asList(Integer.valueOf(SKILLATTR.SKILLATTR_SHUAIRUO.getValue())), Arrays.asList(Integer.valueOf(PETSKILLATTR.PETSKILLATTR_DEBUFFS.getValue())));
        if (flag) {
            List<FightObject> debuffsTargetList = (List)fight.getTargetSide((int)attacker.getId()).stream().filter(obj -> !obj.isFallDown() && PetTriggerPassiveSkill.isMagicAttack(attacker, obj)).sorted(Comparator.comparing(FightObject::getSpeed).reversed()).collect(Collectors.toList());
            for (FightObject object : debuffsTargetList) {
                if (object.getStatusValue(1007) > 0) {
                    return false;
                }
                if (object.getStatusValue(1013) > 0 || object.getStatusValue(1015) > 0 || object.getStatusValue(2070) > 0) {
                    return false;
                }
                continue;
            }
            ((MonsterFightObject)attacker).setPredictTargetList(debuffsTargetList);
            return true;
        }
        return false;
    }
    
    private boolean checkClearWuXing(Fight fight, FightObject attacker, List<RoleSkill> skillIds) {
        List<FightObject> wuXinModificationTargetList = (List)fight.getMySide((int)attacker.getId()).stream().filter(obj -> !obj.isFallDown() && PetPassiveSkillUtil.isAbnormalOrPositiveStatus(obj.getStatusMap(), "wuXinModification")).collect(Collectors.toList());
        if (wuXinModificationTargetList.size() > 0) {
            boolean flag = this.getFlag(attacker, skillIds, null, Arrays.asList(Integer.valueOf(PETSKILLATTR.PETSKILLATTR_RELIEVE_WX.getValue())));
            if (flag) {
                wuXinModificationTargetList = (List)wuXinModificationTargetList.stream().sorted(Comparator.comparing(FightObject::getSpeed).reversed()).collect(Collectors.toList());
                ((MonsterFightObject)attacker).setPredictTargetList(wuXinModificationTargetList);
                return true;
            }
        }
        return false;
    }
    
    private boolean getFlag(FightObject attacker, List<RoleSkill> skillIds, List<Integer> skillAttrs, List<Integer> petSkillAttrs) {
        SysSkill sysSkill = null;
        SysPetSkill petSkill = null;
        boolean flag = false;
        for (RoleSkill roleSkill : skillIds) {
            String indexNumStr = roleSkill.getSkillId().toString().substring(0, 1);
            if ("3".equals(indexNumStr) && skillAttrs != null && skillAttrs.size() > 0) {
                sysSkill = (SysSkill)SkillDataPool.skillMap.get(roleSkill.getSkillId());
                if (sysSkill == null) {
                    continue;
                }
                if (skillAttrs.contains(sysSkill.getAttr())) {
                    flag = true;
                }
            }
            else if ("8".equals(indexNumStr) && petSkillAttrs != null && petSkillAttrs.size() > 0) {
                petSkill = (SysPetSkill)SkillDataPool.petSkillMap.get(roleSkill.getSkillId());
                if (petSkill == null) {
                    continue;
                }
                if (petSkillAttrs.contains(petSkill.getAttr())) {
                    flag = true;
                }
            }
            if (flag) {
                attacker.setSkillId((int)roleSkill.getSkillId());
                return true;
            }
            continue;
        }
        return false;
    }
    
    public boolean isUsePSkill(Fight fight, FightObject attacker) {
        SysSkill sysSkill = (SysSkill)SkillDataPool.skillMap.get(Integer.valueOf(attacker.getSkillId()));
        if (sysSkill == null) {
            return false;
        }
        if (sysSkill != null) {
            List<FightObject> targetList = null;
            if ((int)sysSkill.getAttr() == SKILLATTR.SKILLATTR_FIRE.getValue() || (int)sysSkill.getAttr() == SKILLATTR.SKILLATTR_WIND.getValue() || (int)sysSkill.getAttr() == SKILLATTR.SKILLATTR_THUNDER.getValue() || (int)sysSkill.getAttr() == SKILLATTR.SKILLATTR_WATER.getValue() || (int)sysSkill.getAttr() == SKILLATTR.SKILLATTR_ZHEN.getValue() || (int)sysSkill.getAttr() == SKILLATTR.SKILLATTR_SHUAIRUO.getValue() || (int)sysSkill.getAttr() == SKILLATTR.SKILLATTR_XIXUE.getValue() || (int)sysSkill.getAttr() == SKILLATTR.SKILLATTR_AIHAO.getValue() || (int)sysSkill.getAttr() == SKILLATTR.SKILLATTR_YIWANG.getValue()) {
                targetList = (List)fight.getTargetSide((int)attacker.getId()).stream().filter(obj -> !obj.isFallDown()).collect(Collectors.toList());
            }
            else if ((int)sysSkill.getAttr() == SKILLATTR.SKILLATTR_POISON.getValue() || (int)sysSkill.getAttr() == SKILLATTR.SKILLATTR_SLEEP.getValue() || (int)sysSkill.getAttr() == SKILLATTR.SKILLATTR_CONFUSE.getValue() || (int)sysSkill.getAttr() == SKILLATTR.SKILLATTR_ICE.getValue()) {
                targetList = (List)fight.getTargetSide((int)attacker.getId()).stream().filter(obj -> obj.getStatusValue(1002) > 0 || obj.getStatusValue(1003) > 0 || obj.getStatusValue(1004) > 0 || obj.getStatusValue(1005) > 0 || this.checkOne(obj)).collect(Collectors.toList());
            }
            else if ((int)sysSkill.getAttr() == SKILLATTR.SKILLATTR_PAN.getValue() || (int)sysSkill.getAttr() == SKILLATTR.SKILLATTR_ATTACK.getValue() || (int)sysSkill.getAttr() == SKILLATTR.SKILLATTR_SPEED.getValue()) {
                targetList = (List)fight.getMySide((int)attacker.getId()).stream().filter(obj -> obj.getStatusValue(1010) <= 0 && this.checkTwo(obj)).collect(Collectors.toList());
            }
            if (targetList == null || targetList.size() > 0) {
                return false;
            }
            ((MonsterFightObject)attacker).setPredictTargetList(targetList);
            attacker.setSkillId((int)sysSkill.getSerial());
            return true;
        }
        else {
            return false;
        }
    }
    
    private boolean checkOne(FightObject obj) {
        double wlxs = ImbueUtil.getProValue(obj.getImbueMap(), Integer.valueOf(PRO_DICT_TYPE.WL_XS.getDist()));
        if (wlxs < 0.0) {
            return true;
        }
        double kf = ImbueUtil.getProValue(obj.getImbueMap(), Integer.valueOf(PRO_DICT_TYPE.K_F.getDist()));
        if (kf < 0.0) {
            return true;
        }
        double kl = ImbueUtil.getProValue(obj.getImbueMap(), Integer.valueOf(PRO_DICT_TYPE.K_L.getDist()));
        if (kl < 0.0) {
            return true;
        }
        double ks = ImbueUtil.getProValue(obj.getImbueMap(), Integer.valueOf(PRO_DICT_TYPE.K_S.getDist()));
        if (ks < 0.0) {
            return true;
        }
        double kh = ImbueUtil.getProValue(obj.getImbueMap(), Integer.valueOf(PRO_DICT_TYPE.K_H.getDist()));
        if (kh < 0.0) {
            return true;
        }
        double kzs = ImbueUtil.getProValue(obj.getImbueMap(), Integer.valueOf(PRO_DICT_TYPE.K_ZS.getDist()));
        return kzs < 0.0;
    }
    
    private boolean checkTwo(FightObject obj) {
        double burnHurt = ImbueUtil.getProValue(obj.getImbueMap(), Integer.valueOf(PRO_DICT_TYPE.BURN_HURT.getDist()));
        if (burnHurt <= 0.0) {
            return false;
        }
        double wlxs = ImbueUtil.getProValue(obj.getImbueMap(), Integer.valueOf(PRO_DICT_TYPE.WL_XS.getDist()));
        if (wlxs <= 0.0) {
            return false;
        }
        double kf = ImbueUtil.getProValue(obj.getImbueMap(), Integer.valueOf(PRO_DICT_TYPE.K_F.getDist()));
        if (kf <= 0.0) {
            return false;
        }
        double kl = ImbueUtil.getProValue(obj.getImbueMap(), Integer.valueOf(PRO_DICT_TYPE.K_L.getDist()));
        if (kl <= 0.0) {
            return false;
        }
        double ks = ImbueUtil.getProValue(obj.getImbueMap(), Integer.valueOf(PRO_DICT_TYPE.K_S.getDist()));
        if (ks <= 0.0) {
            return false;
        }
        double kh = ImbueUtil.getProValue(obj.getImbueMap(), Integer.valueOf(PRO_DICT_TYPE.K_H.getDist()));
        if (kh <= 0.0) {
            return false;
        }
        double magicHitRatio = ImbueUtil.getProValue(obj.getImbueMap(), Integer.valueOf(PRO_DICT_TYPE.MAGIC_HIT_RATIO.getDist()));
        if (magicHitRatio <= 0.0) {
            return false;
        }
        double hitRatio = ImbueUtil.getProValue(obj.getImbueMap(), Integer.valueOf(PRO_DICT_TYPE.HIT_RATIO.getDist()));
        if (hitRatio <= 0.0) {
            return false;
        }
        double khl = ImbueUtil.getProValue(obj.getImbueMap(), Integer.valueOf(PRO_DICT_TYPE.K_HL.getDist()));
        if (khl <= 0.0) {
            return false;
        }
        double kdu = ImbueUtil.getProValue(obj.getImbueMap(), Integer.valueOf(PRO_DICT_TYPE.K_DU.getDist()));
        if (kdu <= 0.0) {
            return false;
        }
        double khs = ImbueUtil.getProValue(obj.getImbueMap(), Integer.valueOf(PRO_DICT_TYPE.K_HS.getDist()));
        if (khs <= 0.0) {
            return false;
        }
        double kfy = ImbueUtil.getProValue(obj.getImbueMap(), Integer.valueOf(PRO_DICT_TYPE.K_FY.getDist()));
        return ks > 0.0;
    }
    
    public static void main(String[] args) {
        int j = 0;
        for (int i = 0; i < 10; ++i) {
            switch (j) {
                case 3: {
                    break;
                }
                default: {
                    ++j;
                    break;
                }
            }
        }
    }
    
    public boolean isAllergyMonster(Fight fight, FightObject attacker) {
        List<FightObject> targetSide = fight.getTargetSide((int)attacker.getId());
        targetSide = (List)targetSide.stream().filter(obj -> !obj.isFallDown() && obj.getRace() == RACE.RACE_MO.getValue()).sorted(Comparator.comparing(FightObject::getSpeed).reversed()).collect(Collectors.toList());
        if (targetSide == null || targetSide.size() <= 0) {
            return false;
        }
        FightObject target = (FightObject)targetSide.get(0);
        if (target.getRace() == RACE.RACE_MO.getValue()) {
            return false;
        }
        double percent = 0.05;
        boolean flag = SkillAi.canAffectOnPro(percent);
        if (flag) {
            attacker.setTargetId((int)target.getId());
            return true;
        }
        return false;
    }
    
    public boolean isRunAway(double percent) {
        boolean flag = SkillAi.canAffectOnPro(percent);
        return flag;
    }
    
    public boolean checkSpecialExcute(Fight fight, FightObject attacker) {
        return fight.getSpecialMonsterType() == 1 && attacker.getPos() == 10003;
    }
    
    public void specialExcute(Fight fight, FightObject attacker) {
        if (fight.getSpecialMonsterType() == 1 && attacker.getPos() == 10003 && attacker.getName().equals("地飞星")) {
            try {
                MonsterSkillUtils.beforeAttack(fight, attacker);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            boolean flag = true;
            for (FightObject obj : fight.getMySide((int)attacker.getId())) {
                if (obj.getPos() == attacker.getPos() + 100) {
                    flag = false;
                    break;
                }
                continue;
            }
            if (flag) {
                attacker.setType(3);
                attacker.setSkillId(0);
            }
            else if (attacker.getSkillIds().size() > 0) {
                attacker.setType(7);
                attacker.setSkillId((int)((RoleSkill)attacker.getSkillIds().get(0)).getSkillId());
            }
            else {
                attacker.setType(0);
                attacker.setSkillId(0);
            }
        }
    }
    
    private List<RoleSkill> excludePassiveSkills(List<RoleSkill> skills) {
        if (skills == null || skills.size() <= 0) {
            return null;
        }
        List<RoleSkill> returnList = new ArrayList<>();
        for (RoleSkill skill : skills) {
            if (skill.getSkillId().equals(Integer.valueOf(39006))) {
                returnList.add(skill);
            }
            else {
                if ((int)skill.getSkillId() > 80000 && (int)skill.getSkillId() < 90000) {
                    SysPetSkill petSkill = (SysPetSkill)((Map)PetTriggerPassiveSkill.data_pet_skill_maps.get(PET_PASSIVE_SKILL_TRIGGER_DICT.INITIATIVE_SKILL.getValue())).get(skill.getSkillId());
                    if (petSkill != null && (int)petSkill.getInitiative() > 0) {
                        returnList.add(skill);
                        continue;
                    }
                }
                if ((int)skill.getSkillId() > 30000 && (int)skill.getSkillId() < 40000) {
                    SysSkill roleSkill = (SysSkill)SkillDataPool.skillMap.get(skill.getSkillId());
                    if (roleSkill != null && (int)roleSkill.getInitiative() > 0) {
                        returnList.add(skill);
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
        if (returnList.size() > 0) {
            return returnList;
        }
        return null;
    }
}
