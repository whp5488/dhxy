package com.kitty.system.game.fightnew.book.awaken.other;

import com.kitty.common.utils.BigDecimalUtil;
import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.bean.PetFightObject;
import com.kitty.system.game.fightnew.book.awaken.AwakenSkillProcessor;
import com.kitty.system.game.fightnew.book.awaken.utils.AwakenSkillEffectUtils;
import com.kitty.system.game.fightnew.book.pet.utils.PetSkillEffectorUtil;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.fightnew.utils.ActionParamUtils;
import com.kitty.system.game.fightnew.utils.PassiveSkillUtils;
import com.kitty.system.game.fightnew.utils.SkillAi;
import org.nutz.lang.util.NutMap;

public class AWAKENSKILL_31406 extends AwakenSkillProcessor {
  public void magicAttackEndOther(Fight fight, FightObject effector, Object... args) {
    if (effector.isPet()) {
      NutMap map = AwakenSkillEffectUtils.getPercentDouble((PetFightObject)effector, 70.0D, 20.0D);
      if (map != null) {
        double percent = map.getDouble("percent");
        if (percent > 0.0D && SkillAi.canAffectOnPro(percent)) {
          FightObject target = PassiveSkillUtils.getRandomTarget(fight, effector, (byte)0);
          if (target == null)
            return; 
          SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effector.getId().intValue(), 31406);
          skillRecord.setT(target.getId().intValue());
          int level = effector.getLevel();
          if (level <= 0)
            level = 1; 
          double hurt = BigDecimalUtil.decimalScaleObjectDown(Integer.valueOf(0), Double.valueOf((80 * level) + (effector.getMaxMana() / 100 * 6) * (effector.getRelive() * 0.6D + 1.0D) * 
                PetSkillEffectorUtil.getSuffix(Integer.valueOf(effector.getLevel()), Integer.valueOf(effector.getRelive()), effector.getClose())));
          int harm = PetSkillEffectorUtil.wuXingFireCount(fight, effector, target, hurt);
          ActionParamUtils.changeLife(fight, effector, target, 7, 31406, 2, -harm, false);
        } 
      } 
    } 
  }
}
