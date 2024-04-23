package com.kitty.system.game.fightnew.book.awaken.other;

import com.kitty.system.common.enter.FiedValue;
import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.bean.PetFightObject;
import com.kitty.system.game.fightnew.book.awaken.AwakenSkillProcessor;
import com.kitty.system.game.fightnew.book.awaken.utils.AwakenSkillEffectUtils;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.fightnew.utils.SkillAi;
import java.util.ArrayList;
import java.util.List;
import org.nutz.lang.util.NutMap;

public class AWAKENSKILL_31311 extends AwakenSkillProcessor {
  public List<FiedValue> useDrug(Fight fight, FightObject effector, FightObject target, Object... args) {
    if (args != null && args.length > 0 && !effector.getId().equals(target.getId())) {
      int showClass = ((Integer)args[0]).intValue();
      if (effector.getSkillCoolMap().getInt(String.valueOf(31311)) <= 0 && (1 == showClass || 2 == showClass || 3 == showClass)) {
        NutMap map = AwakenSkillEffectUtils.getPercentDouble((PetFightObject)effector, 20.0D, 20.0D);
        if (map == null)
          return null; 
        double percent = map.getDouble("percent");
        if (percent > 0.0D && SkillAi.canAffectOnPro(percent)) {
          List<FiedValue> list = new ArrayList<>();
          list.add(new FiedValue("addTarget", (byte)6, Boolean.valueOf(true)));
          list.add(new FiedValue("addTargetId", (byte)3, effector.getId()));
          SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effector.getId().intValue(), 31311);
          skillRecord.setT(effector.getId().intValue());
          return list;
        } 
      } 
    } 
    return null;
  }
}
