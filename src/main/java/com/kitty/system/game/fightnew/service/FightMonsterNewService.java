
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
import java.util.function.Function;
import java.util.Comparator;
import java.util.Random;
import java.util.Collection;
import java.util.ArrayList;
import com.kitty.system.game.vo.utils.Const;
import com.kitty.system.game.fightnew.utils.SkillAi;
import com.kitty.system.init.domain.vo.MonsterParameter;
import com.kitty.system.game.role.domain.Role;
import java.util.concurrent.ConcurrentHashMap;
import com.kitty.mina.message.Message;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.enums.war.wardef.LOGIC;
import com.kitty.system.game.fightnew.messages.old.RespEndCombat;
import java.util.Iterator;
import java.util.stream.Collector;
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
    
    public boolean runAway(final Fight fight, final FightObject attacker) {
        final SysDataMonster monster = FightDataPool.data_monster.get(attacker.getSerial());
        if (monster == null) {
            return false;
        }
        if (monster.getEscapeRound() == -1 || fight.getRound() < monster.getEscapeRound()) {
            return false;
        }
        if (attacker.getStatusValue(1002) > 0 && this.isRunAway(monster.getCEscapeRatio())) {
            return true;
        }
        final Iterator<FightObject> iterator = fight.getListA().iterator();
        while (iterator.hasNext()) {
            FightObject obj = iterator.next();
            if (1 == obj.getAutoFight() && this.isRunAway(monster.getCEscapeRatio())) {
                return true;
            }
        }
        if (attacker.getStatusValue(1005) > 0 && this.isRunAway(monster.getEscapeRatio())) {
            return true;
        }
        final List<FightObject> targetSide = fight.getTargetSide(monster.getId()).stream().filter(obj -> !obj.isFallDown() && (RACE.RACE_REN.getValue() == obj.getRace() || RACE.RACE_GUI.getValue() == obj.getRace())).collect(Collectors.toList());
        if (targetSide.size() > 0 && attacker.getStatusValue(1003) <= 0 && this.isRunAway(monster.getEscapeRatio())) {
            return true;
        }
        final List<FightObject> mySide = fight.getMySide(monster.getId()).stream().filter(obj -> !obj.isFallDown()).collect(Collectors.toList());
        boolean isFlag = true;
        for (final FightObject fightObject : mySide) {
            final SysDataMonster mySideMonster = FightDataPool.data_monster.get(fightObject.getSerial());
            if (mySideMonster.getIsBoss() == 0) {
                isFlag = false;
                break;
            }
        }
        return isFlag && attacker.getStatusValue(1003) > 0 && this.isRunAway(monster.getEscapeRatio());
    }
    
    public void monsterFlee(final FightObject fightObject, final Fight fight) {
        final ConcurrentHashMap<Integer, Integer> fightRooms = FightExecutorNewService.FIGHT_ROOMS;
        fight.getListB().remove(fightObject);
        if (fight.getListB() == null || fight.getListB().size() <= 0) {
            final RespEndCombat respEndCombat = new RespEndCombat((short)2);
            for (final FightObject obj : fight.getListA()) {
                if (obj.getLogic() == LOGIC.LOGICTYPE_HERO.getValue()) {
                    final Role role = obj.getRole();
                    MessagePusher.pushMessage(role, respEndCombat);
                }
            }
        }
    }
    
    public boolean isSelectMonster(final Fight fight, final FightObject attacker) {
        if (!attacker.isMonster()) {
            return false;
        }
        final List<FightObject> mySides = fight.getMySide(attacker.getId()).stream().filter(obj -> !obj.isFallDown()).collect(Collectors.toList());
        if (mySides.size() >= 10) {
            return false;
        }
        final SysDataMonster sysDataMonster = FightDataPool.data_monster.get(attacker.getSerial());
        if (sysDataMonster == null) {
            return false;
        }
        final List<MonsterParameter> selectParameterList = sysDataMonster.getSelectParameterList();
        if (selectParameterList == null || selectParameterList.size() <= 0) {
            return false;
        }
        int timeForFight = 0;
        for (final MonsterParameter parameter : selectParameterList) {
            if (fight.getRound() > parameter.getRound()) {
                timeForFight = fight.getMonsterAstrict().getInt(parameter.getType());
                final boolean flag = SkillAi.canAffectOnPro(parameter.getPercent());
                if (flag) {
                    ++timeForFight;
                    fight.getMonsterAstrict().setv(parameter.getType(), (Object)timeForFight);
                }
                return flag;
            }
        }
        return false;
    }
    
    public void selectMonster(final Fight fight, final FightObject attacker) {
        if (this.fightNewService.checkDeStatus(fight, attacker)) {
            return;
        }
        final List<FightObject> targetSide = fight.getTargetSide(attacker.getId()).stream().filter(obj -> !obj.isFallDown()).collect(Collectors.toList());
        if (targetSide == null || targetSide.size() <= 0) {
            return;
        }
        final FightObject targetObject = targetSide.get(0);
        Role role = null;
        if (targetObject.isPlayer()) {
            role = targetObject.getRole();
        }
        else {
            final FightObject targetHostObject = fight.queryFightObject(targetObject.getHostId());
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
        final List<FightObject> mySides = fight.getMySide(attacker.getId()).stream().filter(obj -> !obj.isFallDown()).collect(Collectors.toList());
        final List<Integer> possStatic = Const.poss;
        final List<Integer> poss = new ArrayList<Integer>();
        poss.addAll(possStatic);
        for (final FightObject fightObject : mySides) {
            for (int i = poss.size() - 1; i >= 0; --i) {
                if (fightObject.getPos() == poss.get(i)) {
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
        final Random random = new Random();
        final List<FightObject> monsterList = fight.getListB().stream().sorted(Comparator.comparing(FightObject::getId).reversed()).collect(Collectors.toList());
        int monsterId = monsterList.get(0).getId() + 1;
        for (int j = 0; j < num; ++j) {
            final int posIndex = random.nextInt(poss.size());
            final Integer selectMonsterSerial = ((MonsterFightObject)attacker).getSelectMonsterSerial();
            if (selectMonsterSerial != 0) {
                final SysDataMonster dataMonster = FightDataPool.data_monster.get(selectMonsterSerial);
                final SysDataMonster monster = new SysDataMonster();
                BeanUtils.copyBeanProp(monster, dataMonster);
                monster.setPos(poss.get(posIndex));
                poss.remove(posIndex);
                final MonsterFightObject fightObject2 = new MonsterFightObject(monster, monster.getPos(), monsterId);
                attacker.setTargetId(fightObject2.getId());
                MonsterSkillUtils.star18PetAppear(fight, attacker, fightObject2, monster.getPos(), false);
                ++monsterId;
            }
        }
    }
    
    private boolean selectForStar18(final Fight fight, final FightObject attacker) {
        if (fight.getSpecialMonsterType() == 1 && attacker.getPos() == 10003) {
            final SysDataMonster petMonster = MonsterSkillUtils.getStar18SelectPet();
            if (petMonster != null) {
                MonsterSkillUtils.randomMonsterPetSkill(petMonster);
                petMonster.setPos(attacker.getPos() + 100);
                final List<FightObject> monsterList = fight.getListB().stream().sorted(Comparator.comparing(FightObject::getId).reversed()).collect(Collectors.toList());
                final int monsterId = monsterList.get(0).getId() + 1;
                final MonsterFightObject fightObject = new MonsterFightObject(petMonster, petMonster.getPos(), monsterId);
                MonsterSkillUtils.star18PetAppear(fight, attacker, fightObject, fightObject.getPos(), true);
            }
        }
        return false;
    }
    
    public boolean isUseItem(final Fight fight, final FightObject attacker) {
        final double residueLife = 0.2;
        final double residueMana = 0.2;
        final SysDataMonster sysDataMonster = FightDataPool.data_monster.get(attacker.getSerial());
        if (sysDataMonster == null) {
            return false;
        }
        final List<MonsterParameter> itemUseParameterList = sysDataMonster.getItemUseParameterList();
        if (itemUseParameterList == null || itemUseParameterList.size() <= 0) {
            return false;
        }
        SysDrug sysDrug = null;
        final List<FightObject> mySide = fight.getMySide(attacker.getId());
        List<FightObject> targetList = null;
        FightObject target = null;
        for (final MonsterParameter parameter : itemUseParameterList) {
            if (fight.getRound() <= parameter.getRound()) {
                continue;
            }
            final boolean flag = SkillAi.canAffectOnPro(parameter.getPercent());
            if (!flag) {
                continue;
            }
            sysDrug = ItemDataPool.data_drug.get(parameter.getSerial());
            if (sysDrug == null) {
                continue;
            }
            switch (sysDrug.getShowClass()) {
                case 1: {
                    targetList = mySide.stream().filter(obj -> obj.getSurplusLifePercent() < residueLife).sorted(Comparator.comparing(FightObject::getSurplusLifePercent)).collect(Collectors.toList());
                    if (targetList.size() > 0) {
                        target = targetList.get(0);
                        break;
                    }
                    continue;
                }
                case 2: {
                    targetList = mySide.stream().filter(obj -> obj.getSurplusManaPercent() < residueMana).sorted(Comparator.comparing(FightObject::getSurplusManaPercent)).collect(Collectors.toList());
                    if (targetList.size() > 0) {
                        target = targetList.get(0);
                        break;
                    }
                    continue;
                }
                case 3: {
                    targetList = mySide.stream().filter(obj -> Double.parseDouble(BigDecimalUtil.divide(String.valueOf(obj.getCurrLife()), String.valueOf(obj.getMaxLife()))) < residueLife).sorted(Comparator.comparing(FightObject::getSpeed).reversed()).collect(Collectors.toList());
                    if (targetList.size() > 0) {
                        target = targetList.get(0);
                        break;
                    }
                    continue;
                }
                default: {
                    if (sysDrug.getSerial().equals(10022)) {
                        targetList = fight.getMySide(attacker.getId()).stream().filter(obj -> !obj.isFallDown() && PetPassiveSkillUtil.isAbnormalOrPositiveStatus(obj.getStatusMap(), "abnormal")).sorted(Comparator.comparing(FightObject::getSpeed).reversed()).collect(Collectors.toList());
                        if (targetList.size() <= 0) {
                            continue;
                        }
                        target = targetList.get(0);
                    }
                    if (!sysDrug.getSerial().equals(10024)) {
                        break;
                    }
                    final Iterator<FightObject> iterator2 = mySide.iterator();
                    while (iterator2.hasNext()) {
                        FightObject obj = iterator2.next();
                        final NutMap fightActionRecord = obj.getFightActionRecord();
                        if (fightActionRecord.getInt(fight.getRound() + "details", 0) == 10024) {
                            return false;
                        }
                    }
                    targetList = fight.getTargetSide(attacker.getId()).stream().filter(obj -> obj.getStatusValue(1011) > 0).sorted(Comparator.comparing(FightObject::getSpeed).reversed()).collect(Collectors.toList());
                    if (targetList.size() > 0) {
                        target = targetList.get(0);
                        break;
                    }
                    continue;
                }
            }
            attacker.setSkillId(sysDrug.getSerial());
            attacker.setTargetId(target.getId());
            return true;
        }
        return false;
    }
    
    public boolean isUseSkills(final Fight fight, final FightObject attacker) {
        final SysDataMonster sysDataMonster = FightDataPool.data_monster.get(attacker.getSerial());
        if (sysDataMonster == null) {
            final List<RoleSkill> skills = attacker.getSkillIds().stream().filter(obj -> obj.getSkillId() > 0).collect(Collectors.toList());
            if (skills.size() > 0) {
                final Iterator<RoleSkill> iterator = skills.iterator();
                while (iterator.hasNext()) {
                    RoleSkill skill = iterator.next();
                    final SysPetSkill sysPetSkill = SkillDataPool.petSkillMap.get(skill.getSkillId());
                    if (sysPetSkill == null) {
                        continue;
                    }
                    if (sysPetSkill.getInitiative() > 0 && sysPetSkill.getSerial().equals(skill.getSkillId())) {
                        attacker.setSkillId(skill.getSkillId());
                        return true;
                    }
                }
            }
            return false;
        }
        final double fsGjGl = sysDataMonster.getFsGjGl();
        final boolean flag = SkillAi.canAffectOnPro(fsGjGl);
        if (!flag) {
            return false;
        }
        final List<RoleSkill> skillIds = attacker.getSkillIds().stream().filter(skill -> skill.getSkillId() > 0 && (String.valueOf(skill.getSkillId()).length() == 5 || String.valueOf(skill.getSkillId()).length() == 6)).collect(Collectors.toList());
        boolean isUseSkill = false;
        if (skillIds.size() > 0) {
            if (this.checkCure(fight, attacker, skillIds) || this.checkAbnormal(fight, attacker, skillIds) || this.checkGain(fight, attacker, skillIds) || this.checkObstacle(fight, attacker, skillIds) || this.checkPositive(fight, attacker, skillIds) || this.checkDebuffs(fight, attacker, skillIds) || this.checkClearWuXing(fight, attacker, skillIds)) {
                isUseSkill = true;
            }
            else {
                final List<RoleSkill> roleSkills = skillIds.stream().filter(skill -> this.includeSkill(skill.getSkillId())).collect(Collectors.toList());
                final List<RoleSkill> roleInitiativeSkills = this.excludePassiveSkills(roleSkills);
                if (roleInitiativeSkills != null && roleInitiativeSkills.size() > 0) {
                    final Random rd = new Random();
                    final int i = rd.nextInt(roleInitiativeSkills.size());
                    attacker.setSkillId(roleInitiativeSkills.get(i).getSkillId());
                    isUseSkill = true;
                }
            }
        }
        return isUseSkill;
    }
    
    private boolean includeSkill(final Integer skillId) {
        if (39006 == skillId) {
            return true;
        }
        final List<Integer> SKILLATTRS = new ArrayList<Integer>() {
            {
                this.add(SKILLATTR.SKILLATTR_ZHIYU.getValue());
                this.add(SKILLATTR.SKILLATTR_PAN.getValue());
                this.add(SKILLATTR.SKILLATTR_ATTACK.getValue());
                this.add(SKILLATTR.SKILLATTR_SPEED.getValue());
                this.add(SKILLATTR.SKILLATTR_SLEEP.getValue());
                this.add(SKILLATTR.SKILLATTR_CONFUSE.getValue());
                this.add(SKILLATTR.SKILLATTR_ICE.getValue());
                this.add(SKILLATTR.SKILLATTR_YIWANG.getValue());
                this.add(SKILLATTR.SKILLATTR_SHUAIRUO.getValue());
            }
        };
        final List<Integer> PETSKILLATTRS = new ArrayList<Integer>() {
            {
                this.add(PETSKILLATTR.PETSKILLATTR__ZHIYU.getValue());
                this.add(PETSKILLATTR.PETSKILLATTR_RELIEVE_ABNORMAL.getValue());
                this.add(PETSKILLATTR.PETSKILLATTR_GAIN.getValue());
                this.add(PETSKILLATTR.PETSKILLATTR_RELIEVE_POSITIVE.getValue());
                this.add(PETSKILLATTR.PETSKILLATTR_DEBUFFS.getValue());
                this.add(PETSKILLATTR.PETSKILLATTR_RELIEVE_WX.getValue());
            }
        };
        final String indexNumStr = skillId.toString().substring(0, 1);
        if ("3".equals(indexNumStr)) {
            final SysSkill sysSkill = SkillDataPool.skillMap.get(skillId);
            if (sysSkill == null) {
                return false;
            }
            if (SKILLATTRS.contains(sysSkill.getAttr())) {
                return false;
            }
        }
        else if ("8".equals(indexNumStr)) {
            final SysPetSkill petSkill = SkillDataPool.petSkillMap.get(skillId);
            if (petSkill == null) {
                return false;
            }
            if (PETSKILLATTRS.contains(petSkill.getAttr())) {
                return false;
            }
        }
        return true;
    }
    
    private boolean checkCure(final Fight fight, final FightObject attacker, final List<RoleSkill> skillIds) {
        final List<FightObject> mySide = fight.getMySide(attacker.getId());
        List<FightObject> targetList = mySide.stream().filter(obj -> !obj.isFallDown() && Double.parseDouble(BigDecimalUtil.divide(String.valueOf(obj.getCurrLife()), String.valueOf(obj.getMaxLife()))) < 0.2).collect(Collectors.toList());
        if (targetList.size() > 0) {
            final boolean flag = this.getFlag(attacker, skillIds, Arrays.asList(SKILLATTR.SKILLATTR_ZHIYU.getValue()), Arrays.asList(PETSKILLATTR.PETSKILLATTR__ZHIYU.getValue()));
            if (flag) {
                targetList = targetList.stream().sorted(Comparator.comparing(FightObject::getSpeed).reversed()).collect(Collectors.toList());
                ((MonsterFightObject)attacker).setPredictTargetList(targetList);
                return true;
            }
        }
        return false;
    }
    
    private boolean checkAbnormal(final Fight fight, final FightObject attacker, final List<RoleSkill> skillIds) {
        List<FightObject> abnormalTargetList = fight.getMySide(attacker.getId()).stream().filter(obj -> !obj.isFallDown() && PetPassiveSkillUtil.isAbnormalOrPositiveStatus(obj.getStatusMap(), "abnormal")).collect(Collectors.toList());
        if (abnormalTargetList.size() > 0) {
            final boolean flag = this.getFlag(attacker, skillIds, null, Arrays.asList(PETSKILLATTR.PETSKILLATTR_RELIEVE_ABNORMAL.getValue()));
            if (flag) {
                abnormalTargetList = abnormalTargetList.stream().sorted(Comparator.comparing(FightObject::getSpeed).reversed()).collect(Collectors.toList());
                ((MonsterFightObject)attacker).setPredictTargetList(abnormalTargetList);
                return true;
            }
        }
        return false;
    }
    
    private boolean checkGain(final Fight fight, final FightObject attacker, final List<RoleSkill> skillIds) {
        final boolean flag = this.getFlag(attacker, skillIds, Arrays.asList(SKILLATTR.SKILLATTR_PAN.getValue(), SKILLATTR.SKILLATTR_ATTACK.getValue(), SKILLATTR.SKILLATTR_SPEED.getValue()), Arrays.asList(PETSKILLATTR.PETSKILLATTR_GAIN.getValue(), PETSKILLATTR.PETSKILLATTR_SPEED.getValue()));
        if (flag) {
            final List<FightObject> gainTargetList = fight.getMySide(attacker.getId()).stream().filter(obj -> !obj.isFallDown()).sorted(Comparator.comparing(FightObject::getSpeed).reversed()).collect(Collectors.toList());
            for (final FightObject object : gainTargetList) {
                if (object.getStatusValue(1009) > 0) {
                    return false;
                }
                if (object.getStatusValue(1008) > 0) {
                    return false;
                }
                if (object.getStatusValue(1010) > 0) {
                    return false;
                }
            }
            ((MonsterFightObject)attacker).setPredictTargetList(gainTargetList);
            return true;
        }
        return false;
    }
    
    private boolean checkObstacle(final Fight fight, final FightObject attacker, final List<RoleSkill> skillIds) {
        final boolean flag = this.getFlag(attacker, skillIds, Arrays.asList(SKILLATTR.SKILLATTR_SLEEP.getValue(), SKILLATTR.SKILLATTR_CONFUSE.getValue(), SKILLATTR.SKILLATTR_ICE.getValue(), SKILLATTR.SKILLATTR_YIWANG.getValue()), null);
        if (flag) {
            final List<FightObject> obstacleTargetList = fight.getTargetSide(attacker.getId()).stream().filter(obj -> !obj.isFallDown() && PetTriggerPassiveSkill.isMagicAttack(attacker, obj)).sorted(Comparator.comparing(FightObject::getSpeed).reversed()).collect(Collectors.toList());
            for (final FightObject object : obstacleTargetList) {
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
            }
            ((MonsterFightObject)attacker).setPredictTargetList(obstacleTargetList);
            return true;
        }
        return false;
    }
    
    private boolean checkPositive(final Fight fight, final FightObject attacker, final List<RoleSkill> skillIds) {
        List<FightObject> positiveTargetList = fight.getTargetSide(attacker.getId()).stream().filter(obj -> !obj.isFallDown() && PetTriggerPassiveSkill.isMagicAttack(attacker, obj) && PetPassiveSkillUtil.isAbnormalOrPositiveStatus(obj.getStatusMap(), "positive")).collect(Collectors.toList());
        if (positiveTargetList.size() > 0) {
            final boolean flag = this.getFlag(attacker, skillIds, null, Arrays.asList(PETSKILLATTR.PETSKILLATTR_RELIEVE_POSITIVE.getValue()));
            if (flag) {
                positiveTargetList = positiveTargetList.stream().sorted(Comparator.comparing(FightObject::getSpeed).reversed()).collect(Collectors.toList());
                ((MonsterFightObject)attacker).setPredictTargetList(positiveTargetList);
                return true;
            }
        }
        return false;
    }
    
    private boolean checkDebuffs(final Fight fight, final FightObject attacker, final List<RoleSkill> skillIds) {
        final boolean flag = this.getFlag(attacker, skillIds, Arrays.asList(SKILLATTR.SKILLATTR_SHUAIRUO.getValue()), Arrays.asList(PETSKILLATTR.PETSKILLATTR_DEBUFFS.getValue()));
        if (flag) {
            final List<FightObject> debuffsTargetList = fight.getTargetSide(attacker.getId()).stream().filter(obj -> !obj.isFallDown() && PetTriggerPassiveSkill.isMagicAttack(attacker, obj)).sorted(Comparator.comparing(FightObject::getSpeed).reversed()).collect(Collectors.toList());
            for (final FightObject object : debuffsTargetList) {
                if (object.getStatusValue(1007) > 0) {
                    return false;
                }
                if (object.getStatusValue(1013) > 0 || object.getStatusValue(1015) > 0 || object.getStatusValue(2070) > 0) {
                    return false;
                }
            }
            ((MonsterFightObject)attacker).setPredictTargetList(debuffsTargetList);
            return true;
        }
        return false;
    }
    
    private boolean checkClearWuXing(final Fight fight, final FightObject attacker, final List<RoleSkill> skillIds) {
        List<FightObject> wuXinModificationTargetList = fight.getMySide(attacker.getId()).stream().filter(obj -> !obj.isFallDown() && PetPassiveSkillUtil.isAbnormalOrPositiveStatus(obj.getStatusMap(), "wuXinModification")).collect(Collectors.toList());
        if (wuXinModificationTargetList.size() > 0) {
            final boolean flag = this.getFlag(attacker, skillIds, null, Arrays.asList(PETSKILLATTR.PETSKILLATTR_RELIEVE_WX.getValue()));
            if (flag) {
                wuXinModificationTargetList = wuXinModificationTargetList.stream().sorted(Comparator.comparing(FightObject::getSpeed).reversed()).collect(Collectors.toList());
                ((MonsterFightObject)attacker).setPredictTargetList(wuXinModificationTargetList);
                return true;
            }
        }
        return false;
    }
    
    private boolean getFlag(final FightObject attacker, final List<RoleSkill> skillIds, final List<Integer> skillAttrs, final List<Integer> petSkillAttrs) {
        SysSkill sysSkill = null;
        SysPetSkill petSkill = null;
        boolean flag = false;
        for (final RoleSkill roleSkill : skillIds) {
            final String indexNumStr = roleSkill.getSkillId().toString().substring(0, 1);
            if ("3".equals(indexNumStr) && skillAttrs != null && skillAttrs.size() > 0) {
                sysSkill = SkillDataPool.skillMap.get(roleSkill.getSkillId());
                if (sysSkill == null) {
                    continue;
                }
                if (skillAttrs.contains(sysSkill.getAttr())) {
                    flag = true;
                }
            }
            else if ("8".equals(indexNumStr) && petSkillAttrs != null && petSkillAttrs.size() > 0) {
                petSkill = SkillDataPool.petSkillMap.get(roleSkill.getSkillId());
                if (petSkill == null) {
                    continue;
                }
                if (petSkillAttrs.contains(petSkill.getAttr())) {
                    flag = true;
                }
            }
            if (flag) {
                attacker.setSkillId(roleSkill.getSkillId());
                return true;
            }
        }
        return false;
    }
    
    public boolean isUsePSkill(final Fight fight, final FightObject attacker) {
        final SysSkill sysSkill = SkillDataPool.skillMap.get(attacker.getSkillId());
        if (sysSkill == null) {
            return false;
        }
        if (sysSkill == null) {
            return false;
        }
        List<FightObject> targetList = null;
        if (sysSkill.getAttr() == SKILLATTR.SKILLATTR_FIRE.getValue() || sysSkill.getAttr() == SKILLATTR.SKILLATTR_WIND.getValue() || sysSkill.getAttr() == SKILLATTR.SKILLATTR_THUNDER.getValue() || sysSkill.getAttr() == SKILLATTR.SKILLATTR_WATER.getValue() || sysSkill.getAttr() == SKILLATTR.SKILLATTR_ZHEN.getValue() || sysSkill.getAttr() == SKILLATTR.SKILLATTR_SHUAIRUO.getValue() || sysSkill.getAttr() == SKILLATTR.SKILLATTR_XIXUE.getValue() || sysSkill.getAttr() == SKILLATTR.SKILLATTR_AIHAO.getValue() || sysSkill.getAttr() == SKILLATTR.SKILLATTR_YIWANG.getValue()) {
            targetList = fight.getTargetSide(attacker.getId()).stream().filter(obj -> !obj.isFallDown()).collect(Collectors.toList());
        }
        else if (sysSkill.getAttr() == SKILLATTR.SKILLATTR_POISON.getValue() || sysSkill.getAttr() == SKILLATTR.SKILLATTR_SLEEP.getValue() || sysSkill.getAttr() == SKILLATTR.SKILLATTR_CONFUSE.getValue() || sysSkill.getAttr() == SKILLATTR.SKILLATTR_ICE.getValue()) {
            targetList = fight.getTargetSide(attacker.getId()).stream().filter(obj -> obj.getStatusValue(1002) > 0 || obj.getStatusValue(1003) > 0 || obj.getStatusValue(1004) > 0 || obj.getStatusValue(1005) > 0 || this.checkOne(obj)).collect(Collectors.toList());
        }
        else if (sysSkill.getAttr() == SKILLATTR.SKILLATTR_PAN.getValue() || sysSkill.getAttr() == SKILLATTR.SKILLATTR_ATTACK.getValue() || sysSkill.getAttr() == SKILLATTR.SKILLATTR_SPEED.getValue()) {
            targetList = fight.getMySide(attacker.getId()).stream().filter(obj -> obj.getStatusValue(1010) <= 0 && this.checkTwo(obj)).collect(Collectors.toList());
        }
        if (targetList == null || targetList.size() > 0) {
            return false;
        }
        ((MonsterFightObject)attacker).setPredictTargetList(targetList);
        attacker.setSkillId(sysSkill.getSerial());
        return true;
    }
    
    private boolean checkOne(final FightObject obj) {
        final double wlxs = ImbueUtil.getProValue(obj.getImbueMap(), PRO_DICT_TYPE.WL_XS.getDist());
        if (wlxs < 0.0) {
            return true;
        }
        final double kf = ImbueUtil.getProValue(obj.getImbueMap(), PRO_DICT_TYPE.K_F.getDist());
        if (kf < 0.0) {
            return true;
        }
        final double kl = ImbueUtil.getProValue(obj.getImbueMap(), PRO_DICT_TYPE.K_L.getDist());
        if (kl < 0.0) {
            return true;
        }
        final double ks = ImbueUtil.getProValue(obj.getImbueMap(), PRO_DICT_TYPE.K_S.getDist());
        if (ks < 0.0) {
            return true;
        }
        final double kh = ImbueUtil.getProValue(obj.getImbueMap(), PRO_DICT_TYPE.K_H.getDist());
        if (kh < 0.0) {
            return true;
        }
        final double kzs = ImbueUtil.getProValue(obj.getImbueMap(), PRO_DICT_TYPE.K_ZS.getDist());
        return kzs < 0.0;
    }
    
    private boolean checkTwo(final FightObject obj) {
        final double burnHurt = ImbueUtil.getProValue(obj.getImbueMap(), PRO_DICT_TYPE.BURN_HURT.getDist());
        if (burnHurt <= 0.0) {
            return false;
        }
        final double wlxs = ImbueUtil.getProValue(obj.getImbueMap(), PRO_DICT_TYPE.WL_XS.getDist());
        if (wlxs <= 0.0) {
            return false;
        }
        final double kf = ImbueUtil.getProValue(obj.getImbueMap(), PRO_DICT_TYPE.K_F.getDist());
        if (kf <= 0.0) {
            return false;
        }
        final double kl = ImbueUtil.getProValue(obj.getImbueMap(), PRO_DICT_TYPE.K_L.getDist());
        if (kl <= 0.0) {
            return false;
        }
        final double ks = ImbueUtil.getProValue(obj.getImbueMap(), PRO_DICT_TYPE.K_S.getDist());
        if (ks <= 0.0) {
            return false;
        }
        final double kh = ImbueUtil.getProValue(obj.getImbueMap(), PRO_DICT_TYPE.K_H.getDist());
        if (kh <= 0.0) {
            return false;
        }
        final double magicHitRatio = ImbueUtil.getProValue(obj.getImbueMap(), PRO_DICT_TYPE.MAGIC_HIT_RATIO.getDist());
        if (magicHitRatio <= 0.0) {
            return false;
        }
        final double hitRatio = ImbueUtil.getProValue(obj.getImbueMap(), PRO_DICT_TYPE.HIT_RATIO.getDist());
        if (hitRatio <= 0.0) {
            return false;
        }
        final double khl = ImbueUtil.getProValue(obj.getImbueMap(), PRO_DICT_TYPE.K_HL.getDist());
        if (khl <= 0.0) {
            return false;
        }
        final double kdu = ImbueUtil.getProValue(obj.getImbueMap(), PRO_DICT_TYPE.K_DU.getDist());
        if (kdu <= 0.0) {
            return false;
        }
        final double khs = ImbueUtil.getProValue(obj.getImbueMap(), PRO_DICT_TYPE.K_HS.getDist());
        if (khs <= 0.0) {
            return false;
        }
        final double kfy = ImbueUtil.getProValue(obj.getImbueMap(), PRO_DICT_TYPE.K_FY.getDist());
        return ks > 0.0;
    }
    
    public static void main(final String[] args) {
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
    
    public boolean isAllergyMonster(final Fight fight, final FightObject attacker) {
        List<FightObject> targetSide = fight.getTargetSide(attacker.getId());
        targetSide = targetSide.stream().filter(obj -> !obj.isFallDown() && obj.getRace() == RACE.RACE_MO.getValue()).sorted(Comparator.comparing(FightObject::getSpeed).reversed()).collect(Collectors.toList());
        if (targetSide == null || targetSide.size() <= 0) {
            return false;
        }
        final FightObject target = targetSide.get(0);
        if (target.getRace() == RACE.RACE_MO.getValue()) {
            return false;
        }
        final double percent = 0.05;
        final boolean flag = SkillAi.canAffectOnPro(percent);
        if (flag) {
            attacker.setTargetId(target.getId());
            return true;
        }
        return false;
    }
    
    public boolean isRunAway(final double percent) {
        final boolean flag = SkillAi.canAffectOnPro(percent);
        return flag;
    }
    
    public boolean checkSpecialExcute(final Fight fight, final FightObject attacker) {
        return fight.getSpecialMonsterType() == 1 && attacker.getPos() == 10003;
    }
    
    public void specialExcute(final Fight fight, final FightObject attacker) {
        if (fight.getSpecialMonsterType() == 1 && attacker.getPos() == 10003 && attacker.getName().equals("地飞星")) {
            try {
                MonsterSkillUtils.beforeAttack(fight, attacker);
            }
            catch (final Exception e) {
                e.printStackTrace();
            }
            boolean flag = true;
            for (final FightObject obj : fight.getMySide(attacker.getId())) {
                if (obj.getPos() == attacker.getPos() + 100) {
                    flag = false;
                    break;
                }
            }
            if (flag) {
                attacker.setType(3);
                attacker.setSkillId(0);
            }
            else if (attacker.getSkillIds().size() > 0) {
                attacker.setType(7);
                attacker.setSkillId(attacker.getSkillIds().get(0).getSkillId());
            }
            else {
                attacker.setType(0);
                attacker.setSkillId(0);
            }
        }
    }
    
    private List<RoleSkill> excludePassiveSkills(final List<RoleSkill> skills) {
        if (skills == null || skills.size() <= 0) {
            return null;
        }
        final List<RoleSkill> returnList = new ArrayList<RoleSkill>();
        for (final RoleSkill skill : skills) {
            if (skill.getSkillId() > 80000 && skill.getSkillId() < 90000) {
                final SysPetSkill petSkill = (SysPetSkill)PetTriggerPassiveSkill.data_pet_skill_maps.get(PET_PASSIVE_SKILL_TRIGGER_DICT.INITIATIVE_SKILL.getValue()).get(skill.getSkillId());
                if (petSkill != null && petSkill.getInitiative() > 0) {
                    returnList.add(skill);
                    continue;
                }
            }
            if (skill.getSkillId() > 30000 && skill.getSkillId() < 40000) {
                final SysSkill roleSkill = SkillDataPool.skillMap.get(skill.getSkillId());
                if (roleSkill == null || roleSkill.getInitiative() <= 0) {
                    continue;
                }
                returnList.add(skill);
            }
        }
        if (returnList.size() > 0) {
            return returnList;
        }
        return null;
    }
}
