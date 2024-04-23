package com.kitty.system.game.fightnew.book.awaken.other;

import com.kitty.common.utils.BigDecimalUtil;
import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.bean.PetFightObject;
import com.kitty.system.game.fightnew.book.awaken.AwakenSkillProcessor;
import com.kitty.system.game.fightnew.book.awaken.utils.AwakenSkillEffectUtils;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.fightnew.utils.ActionParamUtils;
import com.kitty.system.game.fightnew.utils.SkillAi;
import org.nutz.lang.util.NutMap;

public class AWAKENSKILL_31408 extends AwakenSkillProcessor {
  public NutMap enteringSide(Fight fight, FightObject effector, Object... args) {
    NutMap map = AwakenSkillEffectUtils.getPercentDouble((PetFightObject)effector, 100.0D, 20.0D);
    if (map != null) {
      double percent = map.getDouble("percent");
      if (percent > 0.0D && SkillAi.canAffectOnPro(percent)) {
        double value = 0.25D;
        int phyPower = effector.getPhyPower();
        String newPhyPowerStr = BigDecimalUtil.multiply(String.valueOf(phyPower), String.valueOf(BigDecimalUtil.additionDouble(1.0D, value)));
        effector.setPhyPower((int)Double.parseDouble(newPhyPowerStr));
        ActionParamUtils.actionForTargetNoth(fight, effector, effector, 7, 31408, 2);
        SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effector.getId().intValue(), 31408);
        skillRecord.setT(effector.getId().intValue());
      } 
    } 
    return null;
  }
}
