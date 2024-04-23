package com.kitty.system.game.fightnew.book.monster.star18;

import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.system.enums.war.wardef.WarMaxRound;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.book.monster.MonsterSkillProcessor;
import com.kitty.system.game.fightnew.messages.old.RespFightShouting;
import com.kitty.system.game.fightnew.model.ActionDetailParam;
import com.kitty.system.game.fightnew.model.FightObjectParam;
import com.kitty.system.game.fightnew.service.FightMessageNewService;
import com.kitty.system.game.fightnew.utils.ActionParamUtils;
import com.kitty.system.game.fightnew.utils.SkillAi;

public class MONSTERSKILL_30042 extends MonsterSkillProcessor {
  public void beforeAttack(Fight fight, FightObject effector, Object... args) {
    if (fight.getRound() == 1 && fight.getSpecialMonsterType() == 1 && effector
      .getPos() == 10003 && effector.getName().equals("地飞星") && effector.getStatusValue(2090) <= 0) {
      if (!SkillAi.canAffectOnPro(0.5D))
        return; 
      int status = 2090;
      int round = WarMaxRound.WAR_MAX_ROUND.getValue();
      effector.setStatus(status, round);
      RespFightShouting fightShouting = ((FightMessageNewService)SpringUtils.getBean(FightMessageNewService.class)).fightShouting(effector, "骑马会会你们", 2000, 1000);
      ActionParamUtils.fightShouting(fight, effector, effector, 10, 10, 2, fightShouting);
      ActionDetailParam actionDetailParam = new ActionDetailParam(effector, 43, 30042, effector.getId().intValue());
      FightObjectParam targetParam = ActionParamUtils.addStatusForTarget(effector, 2, status, round, 1);
      actionDetailParam.getTargetParamList().add(targetParam);
      fight.getActionParam().getActionDetailParams().add(actionDetailParam);
    } 
  }
}
