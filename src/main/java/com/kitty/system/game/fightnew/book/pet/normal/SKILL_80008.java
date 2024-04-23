package com.kitty.system.game.fightnew.book.pet.normal;

import com.kitty.common.utils.BigDecimalUtil;
import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.book.SkillProcessor;
import com.kitty.system.game.fightnew.book.vo.Addition;
import com.kitty.system.game.fightnew.book.vo.Effect;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.fightnew.utils.ActionParamUtils;
import com.kitty.system.game.skill.domain.RoleSkill;
import org.nutz.lang.util.NutMap;

public class SKILL_80008 extends SkillProcessor {
  public NutMap addition(Addition addition, Object... args) {
    return null;
  }
  
  public NutMap passive(Fight fight, FightObject effector, Object... args) {
    SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effector.getId().intValue(), 80008);
    FightObject master = fight.queryFightObject(Integer.valueOf(effector.getHostId()));
    int life = Math.min(master.getCurrLife() + BigDecimalUtil.decimalScaleIntegerDown(Integer.valueOf(0), Double.valueOf(master.getCurrLife() * 0.05D)), master.getMaxLife());
    int addLife = life - master.getCurrLife();
    ActionParamUtils.changeLife(fight, effector, master, 7, 80008, 2, addLife, false);
    skillRecord.setT(master.getId().intValue());
    return null;
  }
  
  public int targetCount(Fight fight, FightObject effector, Object... args) {
    return 1;
  }
  
  public Effect effect(Fight fight, FightObject effector, FightObject target, Object... args) {
    return null;
  }
  
  public int costMana(RoleSkill skill, Object... args) {
    return 0;
  }
}
