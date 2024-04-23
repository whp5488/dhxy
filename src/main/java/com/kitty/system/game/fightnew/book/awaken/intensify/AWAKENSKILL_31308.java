package com.kitty.system.game.fightnew.book.awaken.intensify;

import com.kitty.common.utils.BigDecimalUtil;
import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.bean.PetFightObject;
import com.kitty.system.game.fightnew.book.awaken.AwakenSkillProcessor;
import com.kitty.system.game.fightnew.book.awaken.utils.AwakenSkillEffectUtils;
import com.kitty.system.game.fightnew.imbue.utils.ImbueUtil;
import com.kitty.system.game.fightnew.model.ActionDetailParam;
import com.kitty.system.game.fightnew.model.FightObjectParam;
import com.kitty.system.game.fightnew.model.MagicAttackParam;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.fightnew.utils.SkillAi;
import com.kitty.system.game.skill.domain.RoleSkill;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.nutz.lang.util.NutMap;

public class AWAKENSKILL_31308 extends AwakenSkillProcessor {
  public void magicAttackEnd(Fight fight, FightObject effector, Object... args) {
    if (effector.isPet()) {
      if (args == null && args.length < 3)
        return; 
      RoleSkill skill = (RoleSkill)args[0];
      ActionDetailParam actionDetailParam = (ActionDetailParam)args[1];
      List<MagicAttackParam> targets = (List<MagicAttackParam>)args[2];
      if (skill == null || actionDetailParam == null || targets == null)
        return; 
      NutMap map = AwakenSkillEffectUtils.getPercentDouble((PetFightObject)effector, 50.0D, 20.0D);
      if (map == null)
        return; 
      double percent = map.getDouble("percent");
      if (percent != 0.0D && 
        SkillAi.canAffectOnPro(percent))
        if (effector.getSpeed() > 0) {
          double addCpt = 0.15D;
          int result = (int)Double.parseDouble(BigDecimalUtil.multiply(String.valueOf(effector.getSpeed()), String.valueOf(addCpt)));
          int time = 3;
          effector.setStatus(1010, time);
          ImbueUtil.addOrUpdStatus(effector.getImbueMap(), 1010, PRO_DICT_TYPE.RISE_SPEED.getDist(), time, result);
          FightObjectParam targetParam = actionDetailParam.gainTargetParam(effector.getId().intValue(), true);
          Map<Integer, Integer> statusMap = new HashMap<>();
          statusMap.put(Integer.valueOf(1010), Integer.valueOf(time));
          targetParam.setStatusMap(statusMap);
          SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effector.getId().intValue(), 31308);
          skillRecord.setT(effector.getId().intValue());
        }  
    } 
  }
}
