package com.kitty.system.game.fightnew.book.awaken.other;

import com.kitty.common.utils.BigDecimalUtil;
import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.bean.PetFightObject;
import com.kitty.system.game.fightnew.book.awaken.AwakenSkillProcessor;
import com.kitty.system.game.fightnew.book.awaken.utils.AwakenSkillEffectUtils;
import com.kitty.system.game.fightnew.imbue.utils.ImbueUtil;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.fightnew.utils.ActionParamUtils;
import java.util.ArrayList;
import java.util.List;
import org.nutz.lang.util.NutMap;

public class AWAKENSKILL_31416 extends AwakenSkillProcessor {
  public NutMap enteringSide(Fight fight, FightObject effector, Object... args) {
    NutMap map = AwakenSkillEffectUtils.getPercentDouble((PetFightObject)effector, 15.0D, 20.0D);
    if (map != null) {
      double percent = map.getDouble("percent");
      if (percent > 0.0D) {
        if (percent > 0.3D)
          percent = 0.3D; 
        double selfHit = effector.getSelfAllTempPro(PRO_DICT_TYPE.HIT_RATIO.getDist());
        if (selfHit > 0.0D) {
          double addHit = Double.parseDouble(BigDecimalUtil.multiply(String.valueOf(selfHit), String.valueOf(percent)));
          List<Integer> targets = new ArrayList<>();
          for (FightObject obj : fight.getMySide(effector.getId().intValue())) {
            if (obj.isPet()) {
              ImbueUtil.addRound(obj.getImbueMap(), Integer.valueOf(PRO_DICT_TYPE.HIT_RATIO.getDist()), 2, addHit);
              targets.add(obj.getId());
            } 
          } 
          if (targets.size() > 0) {
            ActionParamUtils.actionForTargetsNoth(fight, effector, targets, 7, 31416, 2);
            SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effector.getId().intValue(), 31416);
            skillRecord.setT(((Integer)targets.get(0)).intValue());
          } 
        } 
      } 
    } 
    return null;
  }
}
