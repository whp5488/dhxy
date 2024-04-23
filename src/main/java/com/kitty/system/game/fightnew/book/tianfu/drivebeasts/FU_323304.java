package com.kitty.system.game.fightnew.book.tianfu.drivebeasts;

import com.kitty.system.enums.war.wardef.OTHER_SKILL_DICT_TYPE;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.DayInCeFight;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.bean.PlayerFightObject;
import com.kitty.system.game.fightnew.bean.TianFuObject;
import com.kitty.system.game.fightnew.book.tianfu.TianFuProcessor;
import com.kitty.system.game.fightnew.book.tianfu.pojo.TianFuAddition;
import com.kitty.system.game.fightnew.book.tianfu.utils.TianFuUtils;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import org.nutz.lang.util.NutMap;

public class FU_323304 extends TianFuProcessor {
  public NutMap addition(TianFuAddition tianFuAddition, Object... args) {
    return null;
  }
  
  public NutMap passive(Fight fight, FightObject effector, FightObject target, Object... args) {
    if (fight.getRound() > 5 || fight.isPvp() != 1 || !effector.isPet() || effector.getType() != 8)
      return null; 
    FightObject theTarget = fight.queryFightObject(Integer.valueOf(effector.getTargetId()));
    if (!theTarget.isPlayer() && !theTarget.isPartner())
      return null; 
    FightObject host = fight.queryFightObject(Integer.valueOf(effector.getHostId()));
    DayInCeFight dayInCeFight = ((PlayerFightObject)host).getDayInCeFight();
    TianFuObject tianFuObject = TianFuUtils.getTianFuObject(dayInCeFight, 323304);
    if (tianFuObject == null)
      return null; 
    FightParamPool.addSkillRecord(fight, 7, effector.getId().intValue(), 323304);
    double percenta = (45.0D + 2.5D * tianFuObject.getLevel()) * (1.0D + dayInCeFight.getLevel() * 0.002D) / 100.0D;
    double percentb = 5.0D * TianFuUtils.getTianFuCount(tianFuObject.getLevel(), dayInCeFight.getLevel()) / 100.0D;
    effector.getBeAttackedMap().setv(String.valueOf(OTHER_SKILL_DICT_TYPE.DRUG_SHARE_PERCENT.getDist()), Double.valueOf(percenta));
    effector.getBeAttackedMap().setv(String.valueOf(OTHER_SKILL_DICT_TYPE.DRUG_SHARE_CHANGE_PERCENT.getDist()), Double.valueOf(percentb));
    return null;
  }
}
