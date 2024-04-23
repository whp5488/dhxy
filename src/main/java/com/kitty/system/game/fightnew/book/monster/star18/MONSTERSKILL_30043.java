package com.kitty.system.game.fightnew.book.monster.star18;

import com.kitty.common.utils.RandomUtil;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.book.monster.MonsterSkillProcessor;
import com.kitty.system.game.fightnew.book.monster.MonsterSkillUtils;
import com.kitty.system.game.fightnew.book.pet.PetPassiveSkillUtil;
import com.kitty.system.game.fightnew.messages.old.RespFightShouting;
import com.kitty.system.game.fightnew.model.ActionDetailParam;
import com.kitty.system.game.fightnew.model.BattleUnit;
import com.kitty.system.game.fightnew.service.FightMessageNewService;
import com.kitty.system.game.fightnew.utils.ActionParamUtils;
import com.kitty.system.game.fightnew.utils.FightUtils;
import com.kitty.system.game.fightnew.utils.SkillAi;
import com.kitty.system.game.fightnew.utils.SkillTargetPicker;
import com.kitty.system.game.skill.domain.RoleSkill;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class MONSTERSKILL_30043 extends MonsterSkillProcessor {
  public void beforeAttack(Fight fight, FightObject effector, Object... args) {
    if (fight.getSpecialMonsterType() == 1 && effector.getPos() == 10003 && effector.getName().equals("地飞星")) {
      if (effector.getSkillCoolMap().getInt(String.valueOf(30043)) > 0)
        return; 
      RespFightShouting fightShouting = ((FightMessageNewService)SpringUtils.getBean(FightMessageNewService.class)).fightShouting(effector, "掌握旷世奇功摄魂大法，真是孤芳自赏啊", 2000, 1000);
      ActionParamUtils.fightShouting(fight, effector, effector, 10, 10, 2, fightShouting);
      effector.getSkillCoolMap().setv(String.valueOf(30043), Integer.valueOf(2));
      int[] attackCounts = { 4, 5, 6 };
      int round = RandomUtil.round(Integer.valueOf(0), Integer.valueOf(attackCounts.length - 1));
      int count = attackCounts[round];
      for (int i = 0; i < count; i++) {
        List<FightObject> targets = SkillTargetPicker.getFightObjects(fight, effector, true);
        if (targets != null && targets.size() > 0) {
          targets = (List<FightObject>)targets.stream().filter(obj -> obj.isPet()).collect(Collectors.toList());
          if (targets != null && targets.size() > 0) {
            int index = RandomUtil.round(Integer.valueOf(0), Integer.valueOf(targets.size() - 1));
            FightObject targetPet = targets.get(index);
            excuteSkill(fight, effector, targetPet);
          } 
        } 
      } 
    } 
  }
  
  private void excuteSkill(Fight fight, FightObject effector, FightObject targetPet) {
    RespFightShouting fightShouting = ((FightMessageNewService)SpringUtils.getBean(FightMessageNewService.class)).fightShouting(effector, "喝啊", 2000, 0);
    ActionParamUtils.fightShouting(fight, effector, effector, 10, 10, 2, fightShouting);
    RoleSkill skillBy = effector.getSkillBy(Integer.valueOf(30043));
    BattleUnit battleUnit = new BattleUnit(fight, effector, targetPet, skillBy);
    battleUnit.setDoubleHit(7);
    ActionDetailParam actionDetailParam = PetPassiveSkillUtil.passivePhysics(fight, effector, targetPet, battleUnit, null, 30043);
    fight.getActionParam().getActionDetailParams().add(actionDetailParam);
    if (!targetPet.isFallDown()) {
      double percent = 0.6D;
      if (SkillAi.canAffectOnPro(percent)) {
        fightShouting = ((FightMessageNewService)SpringUtils.getBean(FightMessageNewService.class)).fightShouting(targetPet, "老大原来你在对面啊", 2000, 1000);
        ActionParamUtils.fightShouting(fight, targetPet, targetPet, 10, 10, 2, fightShouting);
        FightUtils.excutePetDisappear(fight, effector, targetPet);
        int petPos = getPetPos(fight, effector);
        FightObject oldPet = null;
        for (FightObject obj : fight.getMySide(effector.getId().intValue())) {
          if (petPos == obj.getPos()) {
            oldPet = obj;
            break;
          } 
        } 
        List<FightObject> monsterList = (List<FightObject>)fight.getListB().stream().sorted(Comparator.comparing(FightObject::getSerial).reversed()).collect(Collectors.toList());
        int monsterSerial = ((FightObject)monsterList.get(0)).getSerial() + 1;
        targetPet.setSerial(monsterSerial);
        MonsterSkillUtils.star18PetAppear(fight, effector, targetPet, petPos, false);
        if (oldPet != null) {
          fight.removeFightObject(oldPet);
          PetPassiveSkillUtil.sendDisappearData(fight, oldPet);
        } 
      } 
    } 
  }
  
  private int getPetPos(Fight fight, FightObject effector) {
    List<Integer> poss = new ArrayList<Integer>() {
      
      };
    List<Integer> possZ = new ArrayList<>();
    List<FightObject> mySide = fight.getMySide(effector.getId().intValue());
    for (int i = 0; i < poss.size(); i++) {
      for (FightObject obj : mySide) {
        if (obj.getPos() == ((Integer)poss.get(i)).intValue()) {
          possZ.add(poss.get(i));
          break;
        } 
      } 
    } 
    if (possZ.size() >= 2 || possZ.size() <= 0)
      return 10102; 
    poss.remove(possZ.get(0));
    return ((Integer)poss.get(0)).intValue();
  }
}
