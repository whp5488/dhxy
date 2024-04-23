package com.kitty.system.game.fightnew.book.monster;

import com.kitty.common.utils.RandomUtil;
import com.kitty.common.utils.bean.BeanUtils;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.system.enums.war.wardef.FIGHT_TYPE;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.bean.MonsterFightObject;
import com.kitty.system.game.fightnew.book.pet.PetPassiveSkillUtil;
import com.kitty.system.game.fightnew.messages.old.RespFightShouting;
import com.kitty.system.game.fightnew.messages.vo.CombatAddObject;
import com.kitty.system.game.fightnew.messages.vo.FightMember;
import com.kitty.system.game.fightnew.model.ActionDetailParam;
import com.kitty.system.game.fightnew.model.FightObjectParam;
import com.kitty.system.game.fightnew.model.RefreshPet;
import com.kitty.system.game.fightnew.pool.FightDataPool;
import com.kitty.system.game.fightnew.service.FightExecutorNewService;
import com.kitty.system.game.fightnew.service.FightMessageNewService;
import com.kitty.system.game.fightnew.service.FightNewService;
import com.kitty.system.game.fightnew.utils.SkillAi;
import com.kitty.system.game.skill.domain.RoleSkill;
import com.kitty.system.init.domain.SysDataMonster;
import com.kitty.system.init.domain.vo.SkillParameter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class MonsterSkillUtils {
  public static final int[] petMonsterSerialsForStar18 = new int[] { 
      318111, 318112, 318113, 318114, 318115, 318116, 318117, 318118, 318119, 318120, 
      318121 };
  
  public static void beforeAttack(Fight fight, FightObject effector) {
    if (effector.getSkillIds() != null && effector.getSkillIds().size() > 0)
      for (RoleSkill roleSkill : effector.getSkillIds()) {
        if (MonsterSkillConst.getBeforeAttack().contains(roleSkill.getSkillId())) {
          MonsterSkillProcessor monsterSkillProcessor = MonsterSkillProcessor.getMonsterSkill(roleSkill.getSkillId().intValue());
          if (monsterSkillProcessor != null)
            monsterSkillProcessor.beforeAttack(fight, effector, new Object[0]); 
        } 
      }  
  }
  
  public static void randomMonsterPetSkill(SysDataMonster petMonster) {
    ArrayList<SkillParameter> skills = petMonster.getSkills();
    petMonster.setSkills(new ArrayList());
    petMonster.getSkills().addAll(skills);
    if (SkillAi.canAffectOnPro(0.5D))
      addSkill(petMonster, 81031, "隐身"); 
    if (SkillAi.canAffectOnPro(0.5D))
      addSkill(petMonster, 82004, "子虚乌有"); 
  }
  
  public static void addSkill(SysDataMonster monster, int skillSerial, String skillName) {
    SkillParameter skillParameter = new SkillParameter();
    skillParameter.setFightType((byte)0);
    skillParameter.setName(skillName);
    skillParameter.setPriority((byte)1);
    skillParameter.setSerial(skillSerial);
    monster.getSkills().add(skillParameter);
  }
  
  public static SysDataMonster getStar18SelectPet() {
    int[] petMonsters = petMonsterSerialsForStar18;
    int round = RandomUtil.round(Integer.valueOf(0), Integer.valueOf(petMonsters.length - 1));
    int petMonsterSerial = petMonsters[round];
    SysDataMonster petMonster = (SysDataMonster)FightDataPool.data_monster.get(Integer.valueOf(petMonsterSerial));
    SysDataMonster dataMonsterPet = new SysDataMonster();
    BeanUtils.copyBeanProp(dataMonsterPet, petMonster);
    return dataMonsterPet;
  }
  
  public static void star18PetAppear(Fight fight, FightObject effector, FightObject targetPet, int petPos, boolean isSelect) {
    MonsterFightObject monsterFightObject = null;
    targetPet.setPos(petPos);
    ActionDetailParam actionDetailParam = new ActionDetailParam(effector, 3, 0, targetPet.getId().intValue());
    if (isSelect) {
      targetPet.setHostId(effector.getId().intValue());
      effector.setFightPetId(targetPet.getId().intValue());
      effector.setTargetId(targetPet.getId().intValue());
      RefreshPet refreshPet = new RefreshPet();
      ArrayList<RefreshPet> refreshPets = new ArrayList<>();
      refreshPets.add(refreshPet);
      refreshPet.setPetId(targetPet.getId().intValue());
      refreshPet.setType(1);
      actionDetailParam.setPetList(refreshPets);
    } else {
      targetPet.setHostId(0);
      targetPet.setCurrLife(targetPet.getMaxLife());
      targetPet.setTargetId(0);
      targetPet.setASide(false);
      List<FightObject> monsterList = (List<FightObject>)fight.getListB().stream().sorted(Comparator.comparing(FightObject::getId).reversed()).collect(Collectors.toList());
      int monsterId = ((FightObject)monsterList.get(0)).getId().intValue() + 1;
      targetPet.setId(Integer.valueOf(monsterId));
      if (targetPet.isPet()) {
        monsterFightObject = new MonsterFightObject(targetPet, monsterId, targetPet.getSerial(), FIGHT_TYPE.MONSTER.getValue());
        monsterFightObject.setCurrMana(0);
        monsterFightObject.setMaxMana(0);
      } 
    } 
    fight.getListB().add(monsterFightObject);
    FightMember fightMember = monsterFightObject.asFightMember();
    fight.getFightMemberB().add(fightMember);
    FightExecutorNewService.FIGHT_ROOMS.put(monsterFightObject.getId(), Integer.valueOf(fight.getId()));
    List<CombatAddObject> list = new ArrayList<>();
    CombatAddObject combatAddObject = ((FightNewService)SpringUtils.getBean(FightNewService.class)).addObject2Fight(effector.getId().intValue(), (FightObject)monsterFightObject, fight);
    list.add(combatAddObject);
    actionDetailParam.setCombatAddObjects(list);
    RespFightShouting fightShouting = ((FightMessageNewService)SpringUtils.getBean(FightMessageNewService.class)).fightShouting(fight.queryFightObject(monsterFightObject.getId()), "战斗出场");
    FightObjectParam fightObjectParam = actionDetailParam.gainTargetParam(monsterFightObject.getId().intValue(), true);
    fightObjectParam.setFightShouting(fightShouting);
    fight.getActionParam().getActionDetailParams().add(actionDetailParam);
    PetPassiveSkillUtil.otherEnterBattle(fight, monsterFightObject.getId());
  }
}
