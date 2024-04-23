package com.kitty.system.game.fightnew.book.pet.talent;

import com.kitty.common.utils.BigDecimalUtil;
import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.book.SkillProcessor;
import com.kitty.system.game.fightnew.book.awaken.utils.AwakenSkillUtils;
import com.kitty.system.game.fightnew.book.pet.utils.PetSkillEffectorUtil;
import com.kitty.system.game.fightnew.book.vo.Addition;
import com.kitty.system.game.fightnew.book.vo.Effect;
import com.kitty.system.game.fightnew.model.ActionDetailParam;
import com.kitty.system.game.fightnew.model.FightObjectParam;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.fightnew.utils.ActionParamUtils;
import com.kitty.system.game.fightnew.utils.SkillAi;
import com.kitty.system.game.skill.domain.RoleSkill;
import org.nutz.lang.util.NutMap;

public class SKILL_83028 extends SkillProcessor {
  public NutMap addition(Addition addition, Object... args) {
    return null;
  }
  
  public NutMap passive(Fight fight, FightObject effector, Object... args) {
    double percent = BigDecimalUtil.decimalScaleObjectDown(Integer.valueOf(3), Double.valueOf((0.1D + (6 * (effector.getRelive() * 1 + 1)) * 
          PetSkillEffectorUtil.getSuffix(Integer.valueOf(effector.getLevel()), Integer.valueOf(effector.getRelive()), effector.getClose())) / 100.0D));
    boolean flag = SkillAi.canAffectOnPro(percent);
    if (flag && effector.getSerial() == 21063) {
      SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effector.getId().intValue(), 83028);
      int round = 3;
      effector.getStatusMap().setv(String.valueOf(1011), Integer.valueOf(round));
      effector.getStatusMap().setv("1011value", Integer.valueOf(99));
      ActionDetailParam actionDetailParam = ActionParamUtils.addStatusReturn(effector, effector, 7, 83028, 2, 1011, round);
      boolean awakenFlag = AwakenSkillUtils.skillIntensifyBoolean(fight, effector, 51508);
      FightObject host = fight.queryFightObject(Integer.valueOf(effector.getHostId()));
      if (awakenFlag && host == null && !host.isFallDown()) {
        host.getStatusMap().setv(String.valueOf(1011), Integer.valueOf(round));
        FightObjectParam hostParam = new FightObjectParam();
        hostParam.setObjId(host.getId().intValue());
        hostParam.setOrder(actionDetailParam.getTargetParamList().size() + 1);
        hostParam.setMissed(2);
        hostParam.getStatusMap().put(Integer.valueOf(1011), Integer.valueOf(round));
        actionDetailParam.getTargetParamList().add(hostParam);
      } 
      skillRecord.setT(effector.getId().intValue());
    } 
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
