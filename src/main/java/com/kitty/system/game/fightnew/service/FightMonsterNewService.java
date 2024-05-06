package com.kitty.system.game.fightnew.service;

import com.kitty.common.utils.BigDecimalUtil;
import com.kitty.common.utils.bean.BeanUtils;
import com.kitty.logs.LoggerFunction;
import com.kitty.mina.message.Message;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.enums.war.itempro.def.PET_PASSIVE_SKILL_TRIGGER_DICT;
import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.system.enums.war.wardef.LOGIC;
import com.kitty.system.enums.war.wardef.RACE;
import com.kitty.system.enums.war.warskill.skilldef.PETSKILLATTR;
import com.kitty.system.enums.war.warskill.skilldef.SKILLATTR;
import com.kitty.system.game.broadcast.BroadcastService;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.bean.MonsterFightObject;
import com.kitty.system.game.fightnew.book.monster.MonsterSkillUtils;
import com.kitty.system.game.fightnew.book.pet.PetPassiveSkillUtil;
import com.kitty.system.game.fightnew.book.pet.utils.PetTriggerPassiveSkill;
import com.kitty.system.game.fightnew.imbue.utils.ImbueUtil;
import com.kitty.system.game.fightnew.messages.old.RespEndCombat;
import com.kitty.system.game.fightnew.pool.FightDataPool;
import com.kitty.system.game.fightnew.utils.SkillAi;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.skill.domain.RoleSkill;
import com.kitty.system.game.skill.pool.SkillDataPool;
import com.kitty.system.game.vo.utils.Const;
import com.kitty.system.init.domain.SysDataMonster;
import com.kitty.system.init.domain.SysDrug;
import com.kitty.system.init.domain.SysPetSkill;
import com.kitty.system.init.domain.SysSkill;
import com.kitty.system.init.domain.vo.MonsterParameter;
import com.kitty.system.init.pool.ItemDataPool;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import org.nutz.lang.util.NutMap;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FightMonsterNewService {
  private final Logger logger = LoggerFunction.FIGHT.getLogger();
  
  @Autowired
  public FightNewService fightNewService;
  
  @Autowired
  public BroadcastService broadcastService;
  
  public boolean runAway(Fight fight, FightObject attacker) {
    SysDataMonster monster = (SysDataMonster)FightDataPool.data_monster.get(attacker.getSerial());
    if (monster == null)
      return false; 
    if (monster.getEscapeRound() == -1 || fight.getRound() < monster.getEscapeRound())
      return false; 
    if (attacker.getStatusValue(1002) > 0 && 
      isRunAway(monster.getCEscapeRatio()))
      return true; 
    for (FightObject obj : fight.getListA()) {
      if (1 == obj.getAutoFight() && 
        isRunAway(monster.getCEscapeRatio()))
        return true; 
    } 
    if (attacker.getStatusValue(1005) > 0 && 
      isRunAway(monster.getEscapeRatio()))
      return true; 
    List<FightObject> targetSide = (List<FightObject>)fight.getTargetSide(monster.getId()).stream().filter(obj -> (!obj.isFallDown() && (RACE.RACE_REN.getValue() == obj.getRace() || RACE.RACE_GUI.getValue() == obj.getRace()))).collect(Collectors.toList());
    if (targetSide.size() > 0 && attacker.getStatusValue(1003) <= 0 && 
      isRunAway(monster.getEscapeRatio()))
      return true; 
    List<FightObject> mySide = (List<FightObject>)fight.getMySide(monster.getId()).stream().filter(obj -> !obj.isFallDown()).collect(Collectors.toList());
    boolean isFlag = true;
    for (FightObject fightObject : mySide) {
      SysDataMonster mySideMonster = (SysDataMonster)FightDataPool.data_monster.get(fightObject.getSerial());
      if (mySideMonster.getIsBoss() == 0) {
        isFlag = false;
        break;
      } 
    } 
    if (isFlag && attacker.getStatusValue(1003) > 0 && 
      isRunAway(monster.getEscapeRatio()))
      return true; 
    return false;
  }
  
  public void monsterFlee(FightObject fightObject, Fight fight) {
    ConcurrentHashMap<Integer, Integer> fightRooms = FightExecutorNewService.FIGHT_ROOMS;
    fight.getListB().remove(fightObject);
    if (fight.getListB() == null || fight.getListB().size() <= 0) {
      RespEndCombat respEndCombat = new RespEndCombat((short)2);
      for (FightObject obj : fight.getListA()) {
        if (obj.getLogic() == LOGIC.LOGICTYPE_HERO.getValue()) {
          Role role = obj.getRole();
          MessagePusher.pushMessage(role, (Message)respEndCombat);
        } 
      } 
    } 
  }
  
  public boolean isSelectMonster(Fight fight, FightObject attacker) {
    if (!attacker.isMonster())
      return false; 
    List<FightObject> mySides = (List<FightObject>)fight.getMySide(attacker.getId().intValue()).stream().filter(obj -> !obj.isFallDown()).collect(Collectors.toList());
    if (mySides.size() >= 10)
      return false; 
    SysDataMonster sysDataMonster = (SysDataMonster)FightDataPool.data_monster.get(Integer.valueOf(attacker.getSerial()));
    if (sysDataMonster == null)
      return false; 
    List<MonsterParameter> selectParameterList = sysDataMonster.getSelectParameterList();
    if (selectParameterList == null || selectParameterList.size() <= 0)
      return false; 
    int timeForFight = 0;
    for (MonsterParameter parameter : selectParameterList) {
      if (fight.getRound() > parameter.getRound()) {
        timeForFight = fight.getMonsterAstrict().getInt(parameter.getType());
        boolean flag = SkillAi.canAffectOnPro(parameter.getPercent());
        if (flag) {
          timeForFight++;
          fight.getMonsterAstrict().setv(parameter.getType(), Integer.valueOf(timeForFight));
        } 
        return flag;
      } 
    } 
    return false;
  }
  
  public void selectMonster(Fight fight, FightObject attacker) {
    if (this.fightNewService.checkDeStatus(fight, attacker))
      return; 
    List<FightObject> targetSide = (List<FightObject>)fight.getTargetSide(attacker.getId().intValue()).stream().filter(obj -> !obj.isFallDown()).collect(Collectors.toList());
    if (targetSide == null || targetSide.size() <= 0)
      return; 
    FightObject targetObject = targetSide.get(0);
    Role role = null;
    if (targetObject.isPlayer()) {
      role = targetObject.getRole();
    } else {
      FightObject targetHostObject = fight.queryFightObject(Integer.valueOf(targetObject.getHostId()));
      if (targetHostObject != null)
        role = targetHostObject.getRole(); 
    } 
    if (role == null)
      return; 
    attacker.recordAction(fight);
    if (selectForStar18(fight, attacker))
      return; 
    int num = 1;
    List<FightObject> mySides = (List<FightObject>)fight.getMySide(attacker.getId().intValue()).stream().filter(obj -> !obj.isFallDown()).collect(Collectors.toList());
    List<Integer> possStatic = Const.poss;
    List<Integer> poss = new ArrayList<>();
    poss.addAll(possStatic);
    for (FightObject fightObject : mySides) {
      for (int j = poss.size() - 1; j >= 0; j--) {
        if (fightObject.getPos() == ((Integer)poss.get(j)).intValue())
          poss.remove(poss.get(j)); 
      } 
    } 
    if (num > poss.size())
      num = poss.size(); 
    if (num <= 0) {
      attacker.setType(0);
      attacker.setSkillId(0);
      this.fightNewService.normalAttack(attacker, fight);
      return;
    } 
    Random random = new Random();
    List<FightObject> monsterList = (List<FightObject>)fight.getListB().stream().sorted(Comparator.comparing(FightObject::getId).reversed()).collect(Collectors.toList());
    int monsterId = ((FightObject)monsterList.get(0)).getId().intValue() + 1;
    for (int i = 0; i < num; i++) {
      int posIndex = random.nextInt(poss.size());
      Integer selectMonsterSerial = ((MonsterFightObject)attacker).getSelectMonsterSerial();
      if (selectMonsterSerial.intValue() != 0) {
        SysDataMonster dataMonster = (SysDataMonster)FightDataPool.data_monster.get(selectMonsterSerial);
        SysDataMonster monster = new SysDataMonster();
        BeanUtils.copyBeanProp(monster, dataMonster);
        monster.setPos(((Integer)poss.get(posIndex)).intValue());
        poss.remove(posIndex);
        MonsterFightObject fightObject = new MonsterFightObject(monster, Integer.valueOf(monster.getPos()), monsterId);
        attacker.setTargetId(fightObject.getId().intValue());
        MonsterSkillUtils.star18PetAppear(fight, attacker, (FightObject)fightObject, monster.getPos(), false);
        monsterId++;
      } 
    } 
  }
  
  private boolean selectForStar18(Fight fight, FightObject attacker) {
    if (fight.getSpecialMonsterType() == 1 && attacker.getPos() == 10003) {
      SysDataMonster petMonster = MonsterSkillUtils.getStar18SelectPet();
      if (petMonster != null) {
        MonsterSkillUtils.randomMonsterPetSkill(petMonster);
        petMonster.setPos(attacker.getPos() + 100);
        List<FightObject> monsterList = (List<FightObject>)fight.getListB().stream().sorted(Comparator.comparing(FightObject::getId).reversed()).collect(Collectors.toList());
        int monsterId = ((FightObject)monsterList.get(0)).getId().intValue() + 1;
        MonsterFightObject fightObject = new MonsterFightObject(petMonster, Integer.valueOf(petMonster.getPos()), monsterId);
        MonsterSkillUtils.star18PetAppear(fight, attacker, (FightObject)fightObject, fightObject.getPos(), true);
      } 
    } 
    return false;
  }
  
  public boolean isUseItem(Fight fight, FightObject attacker) {
    double residueLife = 0.2D;
    double residueMana = 0.2D;
    SysDataMonster sysDataMonster = (SysDataMonster)FightDataPool.data_monster.get(Integer.valueOf(attacker.getSerial()));
    if (sysDataMonster == null)
      return false; 
    List<MonsterParameter> itemUseParameterList = sysDataMonster.getItemUseParameterList();
    if (itemUseParameterList == null || itemUseParameterList.size() <= 0)
      return false; 
    SysDrug sysDrug = null;
    List<FightObject> mySide = fight.getMySide(attacker.getId().intValue());
    List<FightObject> targetList = null;
    FightObject target = null;
    for (MonsterParameter parameter : itemUseParameterList) {
      if (fight.getRound() <= parameter.getRound())
        continue; 
      boolean flag = SkillAi.canAffectOnPro(parameter.getPercent());
      if (!flag)
        continue; 
      sysDrug = (SysDrug)ItemDataPool.data_drug.get(parameter.getSerial());
      if (sysDrug == null)
        continue; 
      switch (sysDrug.getShowClass().intValue()) {
        case 1:
          targetList = (List<FightObject>)mySide.stream().filter(obj -> (obj.getSurplusLifePercent() < residueLife)).sorted(Comparator.comparing(FightObject::getSurplusLifePercent)).collect(Collectors.toList());
          if (targetList.size() > 0) {
            target = targetList.get(0);
            attacker.setSkillId(sysDrug.getSerial().intValue());
            attacker.setTargetId(target.getId().intValue());
            return true;
          } 
          continue;
        case 2:
          targetList = (List<FightObject>)mySide.stream().filter(obj -> (obj.getSurplusManaPercent() < residueMana)).sorted(Comparator.comparing(FightObject::getSurplusManaPercent)).collect(Collectors.toList());
          if (targetList.size() > 0) {
            target = targetList.get(0);
            attacker.setSkillId(sysDrug.getSerial().intValue());
            attacker.setTargetId(target.getId().intValue());
            return true;
          } 
          continue;
        case 3:
          targetList = (List<FightObject>)mySide.stream().filter(obj -> (Double.parseDouble(BigDecimalUtil.divide(String.valueOf(obj.getCurrLife()), String.valueOf(obj.getMaxLife()))) < residueLife)).sorted(Comparator.comparing(FightObject::getSpeed).reversed()).collect(Collectors.toList());
          if (targetList.size() > 0) {
            target = targetList.get(0);
            attacker.setSkillId(sysDrug.getSerial().intValue());
            attacker.setTargetId(target.getId().intValue());
            return true;
          } 
          continue;
      } 
      if (sysDrug.getSerial().equals(Integer.valueOf(10022))) {
        targetList = (List<FightObject>)fight.getMySide(attacker.getId().intValue()).stream().filter(obj -> (!obj.isFallDown() && PetPassiveSkillUtil.isAbnormalOrPositiveStatus(obj.getStatusMap(), "abnormal"))).sorted(Comparator.comparing(FightObject::getSpeed).reversed()).collect(Collectors.toList());
        if (targetList.size() > 0) {
          target = targetList.get(0);
        } else {
          continue;
        } 
      } 
      if (sysDrug.getSerial().equals(Integer.valueOf(10024))) {
        for (FightObject obj : mySide) {
          NutMap fightActionRecord = obj.getFightActionRecord();
          if (fightActionRecord.getInt(fight.getRound() + "details", 0) == 10024)
            return false; 
        } 
        targetList = (List<FightObject>)fight.getTargetSide(attacker.getId().intValue()).stream().filter(obj -> (obj.getStatusValue(1011) > 0)).sorted(Comparator.comparing(FightObject::getSpeed).reversed()).collect(Collectors.toList());
        if (targetList.size() > 0) {
          target = targetList.get(0);
          attacker.setSkillId(sysDrug.getSerial().intValue());
          attacker.setTargetId(target.getId().intValue());
          return true;
        } 
        continue;
      } 
      attacker.setSkillId(sysDrug.getSerial().intValue());
      attacker.setTargetId(target.getId().intValue());
      return true;
    } 
    return false;
  }
  
  public boolean isUseSkills(Fight fight, FightObject attacker) {
    SysDataMonster sysDataMonster = (SysDataMonster)FightDataPool.data_monster.get(Integer.valueOf(attacker.getSerial()));
    if (sysDataMonster == null) {
      List<RoleSkill> skills = (List<RoleSkill>)attacker.getSkillIds().stream().filter(obj -> (obj.getSkillId().intValue() > 0)).collect(Collectors.toList());
      if (skills.size() > 0)
        for (RoleSkill skill : skills) {
          SysPetSkill sysPetSkill = (SysPetSkill)SkillDataPool.petSkillMap.get(skill.getSkillId());
          if (sysPetSkill == null)
            continue; 
          if (sysPetSkill.getInitiative().intValue() > 0 && sysPetSkill.getSerial().equals(skill.getSkillId())) {
            attacker.setSkillId(skill.getSkillId().intValue());
            return true;
          } 
        }  
      return false;
    } 
    double fsGjGl = sysDataMonster.getFsGjGl();
    boolean flag = SkillAi.canAffectOnPro(fsGjGl);
    if (!flag)
      return false; 
    List<RoleSkill> skillIds = (List<RoleSkill>)attacker.getSkillIds().stream().filter(skill -> (skill.getSkillId().intValue() > 0 && (String.valueOf(skill.getSkillId()).length() == 5 || String.valueOf(skill.getSkillId()).length() == 6))).collect(Collectors.toList());
    boolean isUseSkill = false;
    if (skillIds.size() > 0)
      if (checkCure(fight, attacker, skillIds) || checkAbnormal(fight, attacker, skillIds) || checkGain(fight, attacker, skillIds) || 
        checkObstacle(fight, attacker, skillIds) || checkPositive(fight, attacker, skillIds) || checkDebuffs(fight, attacker, skillIds) || 
        checkClearWuXing(fight, attacker, skillIds)) {
        isUseSkill = true;
      } else {
        List<RoleSkill> roleSkills = (List<RoleSkill>)skillIds.stream().filter(skill -> includeSkill(skill.getSkillId())).collect(Collectors.toList());
        List<RoleSkill> roleInitiativeSkills = excludePassiveSkills(roleSkills);
        if (roleInitiativeSkills != null && roleInitiativeSkills.size() > 0) {
          Random rd = new Random();
          int i = rd.nextInt(roleInitiativeSkills.size());
          attacker.setSkillId(((RoleSkill)roleInitiativeSkills.get(i)).getSkillId().intValue());
          isUseSkill = true;
        } 
      }  
    return isUseSkill;
  }
  
  private boolean includeSkill(Integer skillId) {
    if (39006 == skillId.intValue())
      return true; 
    List<Integer> SKILLATTRS = new ArrayList<Integer>() {
      
      };
    List<Integer> PETSKILLATTRS = new ArrayList<Integer>() {
      
      };
    String indexNumStr = skillId.toString().substring(0, 1);
    if ("3".equals(indexNumStr)) {
      SysSkill sysSkill = (SysSkill)SkillDataPool.skillMap.get(skillId);
      if (sysSkill == null)
        return false; 
      if (SKILLATTRS.contains(sysSkill.getAttr()))
        return false; 
    } else if ("8".equals(indexNumStr)) {
      SysPetSkill petSkill = (SysPetSkill)SkillDataPool.petSkillMap.get(skillId);
      if (petSkill == null)
        return false; 
      if (PETSKILLATTRS.contains(petSkill.getAttr()))
        return false; 
    } 
    return true;
  }
  
  private boolean checkCure(Fight fight, FightObject attacker, List<RoleSkill> skillIds) {
    List<FightObject> mySide = fight.getMySide(attacker.getId());
    List<FightObject> targetList = (List<FightObject>)mySide.stream().filter(obj -> (!obj.isFallDown() && Double.parseDouble(BigDecimalUtil.divide(String.valueOf(obj.getCurrLife()), String.valueOf(obj.getMaxLife()))) < 0.2D)).collect(Collectors.toList());
    if (targetList.size() > 0) {
      boolean flag = getFlag(attacker, skillIds, Arrays.asList(new Integer[] {SKILLATTR.SKILLATTR_ZHIYU.getValue()}), Arrays.asList(new Integer[] {PETSKILLATTR.PETSKILLATTR__ZHIYU.getValue()}));
      if (flag) {
        targetList = (List<FightObject>)targetList.stream().sorted(Comparator.comparing(FightObject::getSpeed).reversed()).collect(Collectors.toList());
        ((MonsterFightObject)attacker).setPredictTargetList(targetList);
        return true;
      } 
    } 
    return false;
  }
  
  private boolean checkAbnormal(Fight fight, FightObject attacker, List<RoleSkill> skillIds) {
    List<FightObject> abnormalTargetList = (List<FightObject>)fight.getMySide(attacker.getId().intValue()).stream().filter(obj -> (!obj.isFallDown() && PetPassiveSkillUtil.isAbnormalOrPositiveStatus(obj.getStatusMap(), "abnormal"))).collect(Collectors.toList());
    if (abnormalTargetList.size() > 0) {
      boolean flag = getFlag(attacker, skillIds, null, Arrays.asList(new Integer[] { Integer.valueOf(PETSKILLATTR.PETSKILLATTR_RELIEVE_ABNORMAL.getValue()) }));
      if (flag) {
        abnormalTargetList = (List<FightObject>)abnormalTargetList.stream().sorted(Comparator.comparing(FightObject::getSpeed).reversed()).collect(Collectors.toList());
        ((MonsterFightObject)attacker).setPredictTargetList(abnormalTargetList);
        return true;
      } 
    } 
    return false;
  }
  
  private boolean checkGain(Fight fight, FightObject attacker, List<RoleSkill> skillIds) {
    boolean flag = getFlag(attacker, skillIds, Arrays.asList(new Integer[] { Integer.valueOf(SKILLATTR.SKILLATTR_PAN.getValue()), Integer.valueOf(SKILLATTR.SKILLATTR_ATTACK.getValue()), Integer.valueOf(SKILLATTR.SKILLATTR_SPEED.getValue()) }), Arrays.asList(new Integer[] { Integer.valueOf(PETSKILLATTR.PETSKILLATTR_GAIN.getValue()), Integer.valueOf(PETSKILLATTR.PETSKILLATTR_SPEED.getValue()) }));
    if (flag) {
      List<FightObject> gainTargetList = (List<FightObject>)fight.getMySide(attacker.getId().intValue()).stream().filter(obj -> !obj.isFallDown()).sorted(Comparator.comparing(FightObject::getSpeed).reversed()).collect(Collectors.toList());
      for (FightObject object : gainTargetList) {
        if (object.getStatusValue(1009) > 0)
          return false; 
        if (object.getStatusValue(1008) > 0)
          return false; 
        if (object.getStatusValue(1010) > 0)
          return false; 
      } 
      ((MonsterFightObject)attacker).setPredictTargetList(gainTargetList);
      return true;
    } 
    return false;
  }
  
  private boolean checkObstacle(Fight fight, FightObject attacker, List<RoleSkill> skillIds) {
    boolean flag = getFlag(attacker, skillIds, Arrays.asList(new Integer[] { Integer.valueOf(SKILLATTR.SKILLATTR_SLEEP.getValue()), Integer.valueOf(SKILLATTR.SKILLATTR_CONFUSE.getValue()), Integer.valueOf(SKILLATTR.SKILLATTR_ICE.getValue()), Integer.valueOf(SKILLATTR.SKILLATTR_YIWANG.getValue()) }), null);
    if (flag) {
      List<FightObject> obstacleTargetList = (List<FightObject>)fight.getTargetSide(attacker.getId().intValue()).stream().filter(obj -> (!obj.isFallDown() && PetTriggerPassiveSkill.isMagicAttack(attacker, obj))).sorted(Comparator.comparing(FightObject::getSpeed).reversed()).collect(Collectors.toList());
      for (FightObject object : obstacleTargetList) {
        if (object.getStatusValue(1004) > 0)
          return false; 
        if (object.getStatusValue(1002) > 0)
          return false; 
        if (object.getStatusValue(1003) > 0)
          return false; 
        if (object.getStatusValue(1006) > 0)
          return false; 
      } 
      ((MonsterFightObject)attacker).setPredictTargetList(obstacleTargetList);
      return true;
    } 
    return false;
  }
  
  private boolean checkPositive(Fight fight, FightObject attacker, List<RoleSkill> skillIds) {
    List<FightObject> positiveTargetList = (List<FightObject>)fight.getTargetSide(attacker.getId().intValue()).stream().filter(obj -> (!obj.isFallDown() && PetTriggerPassiveSkill.isMagicAttack(attacker, obj) && PetPassiveSkillUtil.isAbnormalOrPositiveStatus(obj.getStatusMap(), "positive"))).collect(Collectors.toList());
    if (positiveTargetList.size() > 0) {
      boolean flag = getFlag(attacker, skillIds, null, 
          Arrays.asList(new Integer[] { Integer.valueOf(PETSKILLATTR.PETSKILLATTR_RELIEVE_POSITIVE.getValue()) }));
      if (flag) {
        positiveTargetList = (List<FightObject>)positiveTargetList.stream().sorted(Comparator.comparing(FightObject::getSpeed).reversed()).collect(Collectors.toList());
        ((MonsterFightObject)attacker).setPredictTargetList(positiveTargetList);
        return true;
      } 
    } 
    return false;
  }
  
  private boolean checkDebuffs(Fight fight, FightObject attacker, List<RoleSkill> skillIds) {
    boolean flag = getFlag(attacker, skillIds, Arrays.asList(new Integer[] { Integer.valueOf(SKILLATTR.SKILLATTR_SHUAIRUO.getValue()) }), Arrays.asList(new Integer[] { Integer.valueOf(PETSKILLATTR.PETSKILLATTR_DEBUFFS.getValue()) }));
    if (flag) {
      List<FightObject> debuffsTargetList = (List<FightObject>)fight.getTargetSide(attacker.getId().intValue()).stream().filter(obj -> (!obj.isFallDown() && PetTriggerPassiveSkill.isMagicAttack(attacker, obj))).sorted(Comparator.comparing(FightObject::getSpeed).reversed()).collect(Collectors.toList());
      for (FightObject object : debuffsTargetList) {
        if (object.getStatusValue(1007) > 0)
          return false; 
        if (object.getStatusValue(1013) > 0 || object.getStatusValue(1015) > 0 || object
          .getStatusValue(2070) > 0)
          return false; 
      } 
      ((MonsterFightObject)attacker).setPredictTargetList(debuffsTargetList);
      return true;
    } 
    return false;
  }
  
  private boolean checkClearWuXing(Fight fight, FightObject attacker, List<RoleSkill> skillIds) {
    List<FightObject> wuXinModificationTargetList = (List<FightObject>)fight.getMySide(attacker.getId().intValue()).stream().filter(obj -> (!obj.isFallDown() && PetPassiveSkillUtil.isAbnormalOrPositiveStatus(obj.getStatusMap(), "wuXinModification"))).collect(Collectors.toList());
    if (wuXinModificationTargetList.size() > 0) {
      boolean flag = getFlag(attacker, skillIds, null, Arrays.asList(new Integer[] { Integer.valueOf(PETSKILLATTR.PETSKILLATTR_RELIEVE_WX.getValue()) }));
      if (flag) {
        wuXinModificationTargetList = (List<FightObject>)wuXinModificationTargetList.stream().sorted(Comparator.comparing(FightObject::getSpeed).reversed()).collect(Collectors.toList());
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
        if (sysSkill == null)
          continue; 
        if (skillAttrs.contains(sysSkill.getAttr()))
          flag = true; 
      } else if ("8".equals(indexNumStr) && petSkillAttrs != null && petSkillAttrs.size() > 0) {
        petSkill = (SysPetSkill)SkillDataPool.petSkillMap.get(roleSkill.getSkillId());
        if (petSkill == null)
          continue; 
        if (petSkillAttrs.contains(petSkill.getAttr()))
          flag = true; 
      } 
      if (flag) {
        attacker.setSkillId(roleSkill.getSkillId().intValue());
        return true;
      } 
    } 
    return false;
  }
  
  public boolean isUsePSkill(Fight fight, FightObject attacker) {
    SysSkill sysSkill = (SysSkill)SkillDataPool.skillMap.get(Integer.valueOf(attacker.getSkillId()));
    if (sysSkill == null)
      return false; 
    if (sysSkill != null) {
      List<FightObject> targetList = null;
      if (sysSkill.getAttr().intValue() == SKILLATTR.SKILLATTR_FIRE.getValue() || sysSkill.getAttr().intValue() == SKILLATTR.SKILLATTR_WIND.getValue() || sysSkill
        .getAttr().intValue() == SKILLATTR.SKILLATTR_THUNDER.getValue() || sysSkill.getAttr().intValue() == SKILLATTR.SKILLATTR_WATER.getValue() || sysSkill
        .getAttr().intValue() == SKILLATTR.SKILLATTR_ZHEN.getValue() || sysSkill.getAttr().intValue() == SKILLATTR.SKILLATTR_SHUAIRUO.getValue() || sysSkill
        .getAttr().intValue() == SKILLATTR.SKILLATTR_XIXUE.getValue() || sysSkill.getAttr().intValue() == SKILLATTR.SKILLATTR_AIHAO.getValue() || sysSkill
        .getAttr().intValue() == SKILLATTR.SKILLATTR_YIWANG.getValue()) {
        targetList = (List<FightObject>)fight.getTargetSide(attacker.getId().intValue()).stream().filter(obj -> !obj.isFallDown()).collect(Collectors.toList());
      } else if (sysSkill.getAttr().intValue() == SKILLATTR.SKILLATTR_POISON.getValue() || sysSkill.getAttr().intValue() == SKILLATTR.SKILLATTR_SLEEP.getValue() || sysSkill
        .getAttr().intValue() == SKILLATTR.SKILLATTR_CONFUSE.getValue() || sysSkill.getAttr().intValue() == SKILLATTR.SKILLATTR_ICE.getValue()) {
        targetList = (List<FightObject>)fight.getTargetSide(attacker.getId().intValue()).stream().filter(obj -> (obj.getStatusValue(1002) > 0 || obj.getStatusValue(1003) > 0 || obj.getStatusValue(1004) > 0 || obj.getStatusValue(1005) > 0 || checkOne(obj))).collect(Collectors.toList());
      } else if (sysSkill.getAttr().intValue() == SKILLATTR.SKILLATTR_PAN.getValue() || sysSkill.getAttr().intValue() == SKILLATTR.SKILLATTR_ATTACK.getValue() || sysSkill
        .getAttr().intValue() == SKILLATTR.SKILLATTR_SPEED.getValue()) {
        targetList = (List<FightObject>)fight.getMySide(attacker.getId().intValue()).stream().filter(obj -> (obj.getStatusValue(1010) <= 0 && checkTwo(obj))).collect(Collectors.toList());
      } 
      if (targetList == null || targetList.size() > 0)
        return false; 
      ((MonsterFightObject)attacker).setPredictTargetList(targetList);
      attacker.setSkillId(sysSkill.getSerial().intValue());
      return true;
    } 
    return false;
  }
  
  private boolean checkOne(FightObject obj) {
    double wlxs = ImbueUtil.getProValue(obj.getImbueMap(), Integer.valueOf(PRO_DICT_TYPE.WL_XS.getDist()));
    if (wlxs < 0.0D)
      return true; 
    double kf = ImbueUtil.getProValue(obj.getImbueMap(), Integer.valueOf(PRO_DICT_TYPE.K_F.getDist()));
    if (kf < 0.0D)
      return true; 
    double kl = ImbueUtil.getProValue(obj.getImbueMap(), Integer.valueOf(PRO_DICT_TYPE.K_L.getDist()));
    if (kl < 0.0D)
      return true; 
    double ks = ImbueUtil.getProValue(obj.getImbueMap(), Integer.valueOf(PRO_DICT_TYPE.K_S.getDist()));
    if (ks < 0.0D)
      return true; 
    double kh = ImbueUtil.getProValue(obj.getImbueMap(), Integer.valueOf(PRO_DICT_TYPE.K_H.getDist()));
    if (kh < 0.0D)
      return true; 
    double kzs = ImbueUtil.getProValue(obj.getImbueMap(), Integer.valueOf(PRO_DICT_TYPE.K_ZS.getDist()));
    if (kzs < 0.0D)
      return true; 
    return false;
  }
  
  private boolean checkTwo(FightObject obj) {
    double burnHurt = ImbueUtil.getProValue(obj.getImbueMap(), Integer.valueOf(PRO_DICT_TYPE.BURN_HURT.getDist()));
    if (burnHurt <= 0.0D)
      return false; 
    double wlxs = ImbueUtil.getProValue(obj.getImbueMap(), Integer.valueOf(PRO_DICT_TYPE.WL_XS.getDist()));
    if (wlxs <= 0.0D)
      return false; 
    double kf = ImbueUtil.getProValue(obj.getImbueMap(), Integer.valueOf(PRO_DICT_TYPE.K_F.getDist()));
    if (kf <= 0.0D)
      return false; 
    double kl = ImbueUtil.getProValue(obj.getImbueMap(), Integer.valueOf(PRO_DICT_TYPE.K_L.getDist()));
    if (kl <= 0.0D)
      return false; 
    double ks = ImbueUtil.getProValue(obj.getImbueMap(), Integer.valueOf(PRO_DICT_TYPE.K_S.getDist()));
    if (ks <= 0.0D)
      return false; 
    double kh = ImbueUtil.getProValue(obj.getImbueMap(), Integer.valueOf(PRO_DICT_TYPE.K_H.getDist()));
    if (kh <= 0.0D)
      return false; 
    double magicHitRatio = ImbueUtil.getProValue(obj.getImbueMap(), Integer.valueOf(PRO_DICT_TYPE.MAGIC_HIT_RATIO.getDist()));
    if (magicHitRatio <= 0.0D)
      return false; 
    double hitRatio = ImbueUtil.getProValue(obj.getImbueMap(), Integer.valueOf(PRO_DICT_TYPE.HIT_RATIO.getDist()));
    if (hitRatio <= 0.0D)
      return false; 
    double khl = ImbueUtil.getProValue(obj.getImbueMap(), Integer.valueOf(PRO_DICT_TYPE.K_HL.getDist()));
    if (khl <= 0.0D)
      return false; 
    double kdu = ImbueUtil.getProValue(obj.getImbueMap(), Integer.valueOf(PRO_DICT_TYPE.K_DU.getDist()));
    if (kdu <= 0.0D)
      return false; 
    double khs = ImbueUtil.getProValue(obj.getImbueMap(), Integer.valueOf(PRO_DICT_TYPE.K_HS.getDist()));
    if (khs <= 0.0D)
      return false; 
    double kfy = ImbueUtil.getProValue(obj.getImbueMap(), Integer.valueOf(PRO_DICT_TYPE.K_FY.getDist()));
    if (ks <= 0.0D)
      return false; 
    return true;
  }
  
  public static void main(String[] args) {
    int j = 0;
    for (int i = 0; i < 10; i++) {
      switch (j) {
        case 3:
          break;
        default:
          j++;
          break;
      } 
    } 
  }
  
  public boolean isAllergyMonster(Fight fight, FightObject attacker) {
    List<FightObject> targetSide = fight.getTargetSide(attacker.getId().intValue());
    targetSide = (List<FightObject>)targetSide.stream().filter(obj -> (!obj.isFallDown() && obj.getRace() == RACE.RACE_MO.getValue())).sorted(Comparator.comparing(FightObject::getSpeed).reversed()).collect(Collectors.toList());
    if (targetSide == null || targetSide.size() <= 0)
      return false; 
    FightObject target = targetSide.get(0);
    if (target.getRace() == RACE.RACE_MO.getValue())
      return false; 
    double percent = 0.05D;
    boolean flag = SkillAi.canAffectOnPro(percent);
    if (flag) {
      attacker.setTargetId(target.getId().intValue());
      return true;
    } 
    return false;
  }
  
  public boolean isRunAway(double percent) {
    boolean flag = SkillAi.canAffectOnPro(percent);
    if (flag)
      return true; 
    return false;
  }
  
  public boolean checkSpecialExcute(Fight fight, FightObject attacker) {
    if (fight.getSpecialMonsterType() == 1 && attacker.getPos() == 10003)
      return true; 
    return false;
  }
  
  public void specialExcute(Fight fight, FightObject attacker) {
    if (fight.getSpecialMonsterType() == 1 && attacker.getPos() == 10003 && attacker.getName().equals("地飞星")) {
      try {
        MonsterSkillUtils.beforeAttack(fight, attacker);
      } catch (Exception e) {
        e.printStackTrace();
      } 
      boolean flag = true;
      for (FightObject obj : fight.getMySide(attacker.getId().intValue())) {
        if (obj.getPos() == attacker.getPos() + 100) {
          flag = false;
          break;
        } 
      } 
      if (flag) {
        attacker.setType(3);
        attacker.setSkillId(0);
      } else if (attacker.getSkillIds().size() > 0) {
        attacker.setType(7);
        attacker.setSkillId(((RoleSkill)attacker.getSkillIds().get(0)).getSkillId().intValue());
      } else {
        attacker.setType(0);
        attacker.setSkillId(0);
      } 
    } 
  }
  
  private List<RoleSkill> excludePassiveSkills(List<RoleSkill> skills) {
    if (skills == null || skills.size() <= 0)
      return null; 
    List<RoleSkill> returnList = new ArrayList<>();
    for (RoleSkill skill : skills) {
      if (skill.getSkillId().intValue() > 80000 && skill.getSkillId().intValue() < 90000) {
        SysPetSkill petSkill = (SysPetSkill)((Map)PetTriggerPassiveSkill.data_pet_skill_maps.get(PET_PASSIVE_SKILL_TRIGGER_DICT.INITIATIVE_SKILL.getValue())).get(skill.getSkillId());
        if (petSkill != null && petSkill.getInitiative().intValue() > 0) {
          returnList.add(skill);
          continue;
        } 
      } 
      if (skill.getSkillId().intValue() > 30000 && skill.getSkillId().intValue() < 40000) {
        SysSkill roleSkill = (SysSkill)SkillDataPool.skillMap.get(skill.getSkillId());
        if (roleSkill != null && roleSkill.getInitiative().intValue() > 0)
          returnList.add(skill); 
      } 
    } 
    if (returnList.size() > 0)
      return returnList; 
    return null;
  }
}
